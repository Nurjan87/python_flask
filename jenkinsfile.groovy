node {
    stage("Clone repo"){
        git @github.com:Nurjan87/Flaskex.git
    }
    stage("Build application"){
        sh "scp -r * ec2-useer@${ENV}:/tmp"
        sh "ssh ec2-user@${ENV}  pip install -r /tmp/requirments.txt"
    }
    stage("App Run"){
        sh "ssh ec2-user@${ENV} python /tmp/app.py"
    }
}