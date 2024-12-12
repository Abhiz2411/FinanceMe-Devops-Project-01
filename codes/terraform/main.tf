resource "aws_instance" "master_server" {
  ami           = "ami-09b0a86a2c84101e1"
  instance_type = "t2.medium"
  key_name      = "aws_instance_safe_key_pair"

  # Attached security group: SSH security group(Port:22) and Jenkins server group(Port:8080)
  vpc_security_group_ids = ["sg-096ee8a1bc2bd54d6", "sg-0093eca06d11a0491"]

  tags = {
    Name = "master_server"
  }
}

resource "aws_instance" "build_server" {
  ami           = "ami-09b0a86a2c84101e1"
  instance_type = "t2.medium"
  key_name      = "aws_instance_safe_key_pair"

  # Attached security group: SSH security group(Port:22) and Node Exporter server group(Port:9100)
  vpc_security_group_ids = ["sg-096ee8a1bc2bd54d6", "sg-0adb293d4dbc07a23"]

  tags = {
    Name = "build_server"
  }
}

resource "aws_instance" "prod_server" {
  ami           = "ami-09b0a86a2c84101e1"
  instance_type = "t2.micro"
  key_name      = "aws_instance_safe_key_pair"

  # Attached security group: SSH security group(Port:22), Node Exporter server group(Port:9100) and
  #                          Docker open port secuirty group(Port: 8084)
  vpc_security_group_ids = ["sg-096ee8a1bc2bd54d6", "sg-0adb293d4dbc07a23", "sg-0d2dcc9ae075a030d"]

  tags = {
    Name = "prod_server"
  }
}

resource "aws_instance" "monitoring_server" {
  ami           = "ami-09b0a86a2c84101e1"
  instance_type = "t2.micro"
  key_name      = "aws_instance_safe_key_pair"

  # Attached security group: SSH security group(Port:22), Prometheus server group(Port:9090) and
  #                          Grafana server group(Port:3000)
  vpc_security_group_ids = ["sg-096ee8a1bc2bd54d6", "sg-022986061da6af01d", "sg-0cc3b0a96f4da0c68"]

  tags = {
    Name = "monitoring_server"
  }
}