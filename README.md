# SpringBoot_OAuth2.0
OAuth2.0의 Authentication Server

## 1. git clone 
`https://github.com/jiyoon0701/SpringBoot_OAuth2.0.git`

## 2. Check multi Module 
`authenticationServer(port : 8080), resourceServer(port : 8090), clientServer(port : 9000)`

## 3. clientId 발급 (회원가입 -> 로그인 진행 후)
`http://localhost:8080/api/client/dashboard`

## 4. client가 resource Owner에게 권한 요청
`http://localhost:8080/oauth/authorize?client_id=clientId&redirect_uri=http://localhost:9000/callback&response_type=code&scope=read&state=abc`

## 5. callback uri를 통해 Code 발급
`http://localhost:9000/callback?code=FBZ5U5&state=abc`

## 6. code를 통해 server에게 accessToken 요청
1. `headers 설정 - "Content-Type","application/x-www-form-urlencoded" 컨텐츠 타입은 urlencoding`
2. `clientId:clientSecret@localhost:8080/oauth/token?code=code&grant_type=authorization_code&scope=read&redirect_uri=http://localhost:9000/callback`

## result
![image](https://github.com/jiyoon0701/SpringBoot_OAuth2.0/assets/83527046/89ca1b92-ca42-4433-8012-e9dc90613d3c)
