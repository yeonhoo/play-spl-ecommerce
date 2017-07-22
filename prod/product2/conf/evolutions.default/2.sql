# --- Sample dataset

# --- !Ups

insert into user(email, name, pw) values('admin@scala.com', 'lee', '1234');

insert into user_test(email, name, cpf, date_of_birth, address, password) values('admin@scala.com', 'lee', '123-456-789-00', '01-01-1970', 'Rua Lauro Linhares 2000', '1234');

insert into category (id,name,url,description) values ( 1,'eletrônico', 'jogo', 'jogos em geral');
insert into category (id,name,url,description) values ( 2,'eletrônico', 'livro', 'ebooks');
insert into category (id,name,url,description) values ( 3,'eletrônico', 'cosmet', 'cosmeticos importados');
insert into category (id,name,url,description) values ( 4,'eletrônico', 'roupa', 'roupas importadas');

insert into product (id,name,description,publish,category_id) values (1,'iPhone 4S', 'celular', '2011-10-14',1);
insert into product (id,name,description,publish,category_id) values (2,'galaxy 4S', 'celular', '2012-10-14',1);
insert into product (id,name,description,publish,category_id) values (3,'LG G5', 'celular', '2013-10-14',2);
insert into product (id,name,description,publish,category_id) values (4,'Motorola', 'celular', '2014-10-14',3);
insert into product (id,name,description,publish,category_id) values (5,'Asus', 'celular', '2015-10-14',4);
insert into product (id,name,description,publish,category_id) values (6,'Asus', 'celular', '2015-10-14',4);
insert into product (id,name,description,publish,category_id) values (7,'Asus', 'celular', '2015-10-14',4);
insert into product (id,name,description,publish,category_id) values (8,'Asus', 'celular', '2015-10-14',4);
insert into product (id,name,description,publish,category_id) values (9,'Asus', 'celular', '2015-10-14',4);
insert into product (id,name,description,publish,category_id) values (10,'Asus', 'celular', '2015-10-14',4);




# --- !Downs

delete from product;
delete from category;

delete from computer;
delete from company;
