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
        sh "ssh ec2-user@${ENV}  sudo pip install -r /tmp/requirements.txt"
    }
    stage("Create Folder"){        
        sh "ssh ec2-user@${ENV} sudo mkdir -p /flaskex"
    }
    // block to work on next 
     stage("Write to a file"){
        sh "ssh ec2-user@${ENV} echo [Unit] > /tmp/flaskex.service"
        sh "ssh ec2-user@${ENV} echo After=network.target >> /tmp/flaskex.service"
        sh "ssh ec2-user@${ENV} echo [Service] >> /tmp/flaskex.service"
        sh "ssh ec2-user@${ENV} echo Type=simple >> /tmp/flaskex.service"
        sh "ssh ec2-user@${ENV} echo ExecStart=/bin/python /flaskex/app.py  >> /tmp/flaskex.service"
        sh "ssh ec2-user@${ENV} echo Restart=on-abort >> /tmp/flaskex.service"
        sh "ssh ec2-user@${ENV} echo [Install] >> /tmp/flaskex.service"
        sh "ssh ec2-user@${ENV} echo WantedBy=multi-user.target >> /tmp/flaskex.service"
    }

    // block to work on next
    stage("Copy to System"){
        sh "ssh ec2-user@${ENV} sudo cp -r /tmp/flaskex.service /etc/systemd/system"
    }    
    stage("move files to /flaskex"){
        sh "ssh ec2-user@${ENV} sudo cp -r /tmp/* /flaskex"{
            try {
                sh "ssh ec2-user@${ENV} sudo cp -r /tmp/*flaskex"
            }
            catch(err){
                sh "eho did not copy"
            }
    }
    stage("Install requiremennts"){
        sh "ssh ec2-user@${ENV} sudo pip install -r /flaskex/requirements.txt"
    }
    stage("App Run"){
        sh "ssh ec2-user@${ENV} sudo  systemctl start flaskex"
    }
}
