/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.quorum.gauge;

import com.quorum.gauge.common.Context;
import com.quorum.gauge.common.QuorumNode;
import com.quorum.gauge.core.AbstractSpecImplementation;
import com.quorum.gauge.services.RaftService;
import com.thoughtworks.gauge.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.tx.Contract;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class BlockGasLimit extends AbstractSpecImplementation {

    @Autowired
    RaftService raftService;

    @Step("Send <numberTxns> private transactions where minter is not a participant and gas is equal to gas limit")
    public void sendPrivateTransactions(String numberTxns) {
        // Get the block gasLimit, we will use this as the gas for each transaction
        EthBlock block = utilService.getBlock(QuorumNode.Node1, "latest").toBlocking().first();

        // Don't want minting node to be a participant to the private contract
        QuorumNode minter = raftService.getLeader(QuorumNode.Node1);
        QuorumNode source = QuorumNode.Node1;
        QuorumNode target = QuorumNode.Node2;
        if (source.equals(minter) || target.equals(minter)) {
            source = QuorumNode.Node3;
            target = QuorumNode.Node4;
        }

        int arbitraryValue = new Random().nextInt(50) + 1;
        List<Observable<? extends Contract>> allObservableContracts = new ArrayList<>();
        for (int i = 0; i < Integer.valueOf(numberTxns); i++) {
            allObservableContracts.add(contractService.createSimpleContract(arbitraryValue, source, target, block.getBlock().getGasLimit())
                    .doAfterTerminate(() -> Context.clear())
                    .subscribeOn(Schedulers.io()));
        }

        List<Contract> contracts = Observable.zip(allObservableContracts, args -> {
            List<Contract> tmp = new ArrayList<>();
            for (Object o : args) {
                tmp.add((Contract) o);
            }
            return tmp;
        }).toBlocking().first();
    }

}
