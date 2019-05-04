node {
    properties([parameters([string(defaultValue: 'IP', description: 'where to build', name: 'ENV', trim: false)])])
    stage("Clone repo"){
        git 'git@github.com:Nurjan87/Flaskex.git'
    }
    stage("Build application"){

        sh "scp -r * ec2-user@${ENV}:/tmp"
        sh "ssh ec2-user@${ENV} sudo pip install -r /tmp/requirements.txt"
    }
    stage("App Run"){
        sh "ssh ec2-user@${ENV} python /tmp/app.py"
    }
}