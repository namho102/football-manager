ALTER PROCEDURE addUser
    @username nchar(100), 
    @password  NVARCHAR(50), 
    @role  nchar(10) = NULL
AS
BEGIN
    SET NOCOUNT ON

    BEGIN TRY

        INSERT INTO Users (username, password, role)
        VALUES(@username, HASHBYTES('MD5', @password), @role)

    END TRY
    BEGIN CATCH
    END CATCH

END


EXEC dbo.addUser @username='namho', @password= N'abcdef', @role='admin'	

select * from Users where password =  HASHBYTES('MD5', 'abcdef')

ALTER TABLE Users
ALTER  COLUMN password BINARY(16)

--delete from Users