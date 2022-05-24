const AWS = require('aws-sdk');
const {createServer} = require('dynamodb-admin');

const dynamodb = new AWS.DynamoDB({
	region: 'ap-northeast-2'
});
const dynClient = new AWS.DynamoDB.DocumentClient({service: dynamodb});

const app = createServer(dynamodb, dynClient);

const host = 'localhost';
const port = 8000;
const server = app.listen(port, host);
server.on('listening', () => {
  const address = server.address();
  console.log(`  listening on http://${address.address}:${address.port}`);
});