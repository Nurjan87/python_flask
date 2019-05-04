node{
    properties([parameters([string(defaultValue: 'IP', description: 'Where to build e.g IP', name: 'ENV', trim: true)])])
    stage("Clone repo"){
        git 'git@github.com:Nurjan87/Flaskex.git'
    }
    stage("Remove /tmp content"){
        sh "ssh ec2-user@${ENV} sudo rm -rf /tmp/*"
    }
    stage("Copy filese over"){
        sh "scp -r * ec2-user@${ENV}:/tmp"
    }
    stage("Create Folder"){        
        sh "ssh ec2-user@${ENV} sudo mkdir -p /flaskex"
    }
    stage("Copy to System"){
        sh "ssh ec2-user@${ENV} sudo cp -r /tmp/flaskex.service /etc/systemd/system"
    }    
    stage("move files to /flaskex"){
        sh "ssh ec2-user@${ENV} sudo cp -r /tmp/* /flaskex"
    }
    stage("Install requiremennts"){
        sh "ssh ec2-user@${ENV} sudo pip install -r /flaskex/requirements.txt"
    }
    stage("App Run"){
        sh "ssh ec2-user@${ENV}  systemctl start flaskex"
    }
}
