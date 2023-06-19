-- oauth2_client_details 테이블
CREATE TABLE oauth2_client_details (
                                      client_id VARCHAR(256) PRIMARY KEY,
                                      client_secret VARCHAR(256),
                                      scope VARCHAR(256),
                                      authorized_grant_types VARCHAR(256),
                                      authorities VARCHAR(256),
                                      access_token_validity INTEGER,
                                      refresh_token_validity INTEGER,
                                      additional_information VARCHAR(4096),
                                      autoapprove VARCHAR(256)
);

-- oauth2_access_token 테이블
CREATE TABLE oauth2_access_token (
                                    token_id VARCHAR(256),
                                    token BLOB,
                                    authentication_id VARCHAR(256) PRIMARY KEY,
                                    user_name VARCHAR(256),
                                    client_id VARCHAR(256),
                                    authentication BLOB,
                                    refresh_token VARCHAR(256)
);

-- oauth2_refresh_token 테이블
CREATE TABLE oauth2_refresh_token (
                                     token_id VARCHAR(256),
                                     token BLOB,
                                     authentication BLOB
);

-- oauth2_code 테이블
CREATE TABLE oauth2_code (
                            code VARCHAR(256),
                            authentication BLOB
);

-- oauth2_approvals 테이블
CREATE TABLE oauth_approvals (
                                 userId VARCHAR(256),
                                 clientId VARCHAR(256),
                                 scope VARCHAR(256),
                                 status VARCHAR(10),
                                 expiresAt TIMESTAMP,
                                 lastModifiedAt TIMESTAMP
);

-- oauth2_client_token 테이블
CREATE TABLE oauth2_client_token (
                                    token_id VARCHAR(256),
                                    token BLOB,
                                    authentication_id VARCHAR(256) PRIMARY KEY,
                                    user_name VARCHAR(256),
                                    client_id VARCHAR(256)
);

-- oauth2_client_approval 테이블
CREATE TABLE oauth2_client_approval (
                                       clientId VARCHAR(256),
                                       userId VARCHAR(256),
                                       scope VARCHAR(256)
);

-- oauth2_client_redirect_uri 테이블
CREATE TABLE oauth2_client_redirect_uri (
                                           clientId VARCHAR(256),
                                           redirectUri VARCHAR(256)
);

-- oauth2_client_resource 테이블
CREATE TABLE oauth2_client_resource (
                                       clientId VARCHAR(256),
                                       resourceId VARCHAR(256)
);

-- oauth2_client_scope 테이블
CREATE TABLE oauth2_client_scope (
                                    clientId VARCHAR(256),
                                    scope VARCHAR(256)
);
