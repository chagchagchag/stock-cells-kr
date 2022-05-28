



# 디렉터리 생성, npm init

`dynamodb_admin` 디렉터리를 생성하고 해당 디렉터리를 `npm init` 한다.

```bash
 mkdir dynamodb_admin
 cd dynamodb_admin
 npm init
 # vscode 를 환경변수로 등록해두었다면, 현재 디렉터리를 vscode로 연다
 code . 
```

<br>

# 패키지 설치

```bash
# 방금 만들어둔 디렉터리로 이동
cd dynamodb_admin
npm install aws-sdk
npm install dynamodb-admin
```

<br>

# 어드민 프로그램 작성 & 구동

내 경우는 로컬에 도커로 띄워둔 DynamoDB의 port가 5555 이기에 아래와 같이 5555 를 포트번호로 지정해두었다.<br>

```bash
const AWS = require('aws-sdk');
const {createServer} = require('dynamodb-admin');

const dynamodb = new AWS.DynamoDB({
	region: 'ap-northeast-2'
});
const dynClient = new AWS.DynamoDB.DocumentClient({service: dynamodb});

const app = createServer(dynamodb, dynClient);

const host = 'localhost';
const port = 5555;
const server = app.listen(port, host);
server.on('listening', () => {
  const address = server.address();
  console.log(`  listening on http://${address.address}:${address.port}`);
});
```

<br>

# 어드민 페이지 접속





