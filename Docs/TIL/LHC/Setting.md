# Setting을 위한 README

## Django setting

### 가상환경 실행

```cmd
conda env list # 콘다 가상환경 서버 확인

conda activate [서버명] # 콘다 가상환경 실행해 파이썬 버전 3.7 이상을 사용할 수 있도록 해준다.
```

### requirements.txt 설치

```cmd
# backend 폴더로 들어가 dir(ls) 검색해 requirements.txt 파일과 manage.py 파일이 있는지 확인해 준다.
pip install -r requirements.txt
# 세팅완료

# 서버실행
python manage.py runserve
```



## Aws 서버 mysql setting

```cmd
# cmd 켜기
sudo su
apt-get-update

apt-get install mysql-server
mysql -u root -p
# 비밀번호 입력

sudo su
cd /etc/mysql/mysql.conf.d
vi mysqld.cnf

service mysql restart
mysql -u root -p
#비밀번호 입력

# 접속 권한 부여
mysql> create user ari@localhost identified by 'password'; # 패스워드 설정
mysql> grant all privileges on test.* to ari@localhost;
flush privileges;
```

- 참고사이트
  - https://luji.tistory.com/7
  - https://javagwanjin.tistory.com/entry/MYSQL-%EC%9C%A0%EC%A0%80%EB%A7%8C%EB%93%A4%EA%B8%B0

![image-20210323142116252](Setting.assets/image-20210323142116252.png)

- [ubuntu@j4a402.p.ssafy.io](mailto:ubuntu@j4a402.p.ssafy.io)

- connection name 임의설정

- username: ubuntu

- hostname: j4a402.p.ssafy.io

- ssh key file 넣기

- username 임의 설정

- Test Connection(확인) - OK

  

## Django와 mysql 연동



```python
# pip install mysqlclient

DATABASES = {
    'default': {
        'ENGINE': 'django.db.backends.mysql',
        'NAME': 'test_db',
        'USER': 'ari',
        'PASSWORD': 'ssafy',
        'HOST': 'j4a402.p.ssafy.io',
        'PORT': '3306',
        'OPTIONS': {
            'init_command': 'SET sql_mode="STRICT_TRANS_TABLES"'
        }
    }
}
```

- user의 hostname 확인
- mysql -u ari -p
- select host from mysql.user where user='username';
- host name 이  `local` 인 경우 `%` 로 변경
  - (참고) user 테이블에서는 user, host, authentication_string 정도가 볼만한 컬럼입니다. (username, ip, password)

