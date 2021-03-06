
resource "docker_container" "ethstats" {
  depends_on = [docker_image.local, docker_image.registry]
  image    = var.ethstats.container.image.name
  name     = format("%s-ethstats", var.network_name)
  hostname = "ethstats"
  ports {
    internal = var.ethstats.container.port
  }
  ports {
    internal = var.ethstats.container.port
    external = var.ethstats.host.port
  }
  env = ["WS_SECRET=${var.ethstats_secret}"]
  networks_advanced {
    name         = docker_network.quorum.name
    ipv4_address = var.ethstats_ip
    aliases      = ["ethstats"]
  }
  healthcheck {
    test         = ["CMD", "nc", "-vz", "localhost", var.ethstats.container.port]
    interval     = "3s"
    retries      = 10
    timeout      = "3s"
    start_period = "3s"
  }
}