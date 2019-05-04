node{
    properties([parameters([string(defaultValue: '18.219.84.148', description: 'IP of the host', name: 'my_env', trim: true)])])
    stage("Clone Repo"){
        git "git@github.com:Nurjan87/stormpath_paython.git"
    }
    stage("Clean tmp folder"){
        sh "ssh ec2-user@${my_env} rm -rf /tmp/*"   
    }
    stage("Create neccessary folders"){
        sh "ssh ec2-user@${my_env} mkdir -p /tmp/second"
        sh "ssh ec2-user@${my_env} sudo mkdir -p /flaskex"
    }
    stage("Copy files to tmp"){
        sh "scp -r * ec2-user@${my_env}:/tmp/second"
    }
    stage("Creating a service"){
        try{
            sh "ssh ec2-user@${my_env} echo Description=flask >> /tmp/flaskex.service"
            sh "ssh ec2-user@${my_env} echo After=network.target >> /tmp/flaskex.service"
            sh "ssh ec2-user@${my_env} echo Type=simple >> /tmp/flaskex.service"
            sh "ssh ec2-user@${my_env} echo 'ExecStart=/bin/python /flaskex/app.py'  >> /tmp/flaskex.service"
            sh "ssh ec2-user@${my_env} echo Restart=on-abort >> /tmp/flaskex.service"
            sh "ssh ec2-user@${my_env} echo [Install] >> /tmp/flaskex.service"
            sh "ssh ec2-user@${my_env} echo WantedBy=multi-user.target >> /tmp/flaskex.service"
        }
        catch(err)
        {
            sh "echo errrrrrrrrrrror"
        }
    }
    stage("Copy files from tmp to respective folders"){
        sh "ssh ec2-user@${my_env} sudo cp -r /tmp/flaskex.service /etc/systemd/system"
        sh "ssh ec2-user@${my_env} sudo cp -r /tmp/second/* /flaskex"
    }
    stage("Install Rrequirements"){
        sh "ssh ec2-user@${my_env} sudo pip install -r /tmp/second/requirements.txt"
    }
    stage("Run app"){
        sh "ssh ec2-user@${my_env} sudo systemctl start flaskex"
    }
}
