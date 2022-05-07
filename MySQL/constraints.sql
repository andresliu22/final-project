use db_fp;

ALTER TABLE employee ADD CONSTRAINT FK_employee_user FOREIGN KEY(user_id)
REFERENCES user(user_id);

ALTER TABLE address ADD CONSTRAINT FK_address_employee FOREIGN KEY(employee_id)
REFERENCES employee(employee_id);

ALTER TABLE emergency_contact ADD CONSTRAINT FK_emergency_contact_employee FOREIGN KEY(employee_id)
REFERENCES employee(employee_id);
