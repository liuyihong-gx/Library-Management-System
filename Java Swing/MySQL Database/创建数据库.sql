create table books
(
	ISBN char(13) primary key,
	book_title varchar(50),
	book_type varchar(8),
	book_num int,
	book_desc varchar(1024),
	borrowed_num int

)

create table book
(
	ISBN char(13),
	book_id char(6) primary key,
	in_or_out bit,
	reserved bit,
	foreign key (ISBN) references books (ISBN)
)

create table readers
(
	card_id char(10) primary key,
	reader_name varchar(50),
	phone_num char(11),
	reader_status char(4),
	can_borrow smallint,
	already_borrow smallint
)

create table borrow_info
(
	card_id char(10),
	book_id char(6),
	borrow_date date,
	primary key(card_id, book_id),
	foreign key (card_id) references readers (card_id),
	foreign key (book_id) references book (book_id)
)

create table return_info
(
	card_id char(10),
	book_id char(6),
	return_date date,
	primary key(card_id, book_id),
	foreign key (card_id) references readers (card_id),
	foreign key (book_id) references book (book_id)
)

