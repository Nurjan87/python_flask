[Unit]
Description=flask
After=network.target

[Service]
Type=simple
ExecStart=/bin/python /flaskex/app.py 
Restart=on-abort

[Install]
WantedBy=multi-user.target