/**
 * CREATE Script for init of DB
 */

-- Create an authorized user

INSERT INTO user(id, username, password, token)
VALUES (1 , 'user' , 'password' , 'fc81570e-d210-11e7-8941-cec278b6b50a');
