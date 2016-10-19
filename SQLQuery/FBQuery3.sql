ALTER PROCEDURE addUser
    @username nchar(100), 
    @password  VARCHAR(50), 
    @role  nchar(10) = NULL
AS
BEGIN
    SET NOCOUNT ON

    BEGIN TRY
		print @password
		DECLARE @hash_password NVARCHAR(32);       
		
		print HashBytes('MD5', @password)

		SET @hash_password = CONVERT(NVARCHAR(32), HashBytes('MD5', @password), 2);
		print @hash_password

        INSERT INTO Users (username, password, role) VALUES(@username, @hash_password, @role)		 

    END TRY
    BEGIN CATCH
    END CATCH

END


EXEC addUser @username='namho2', @password= '123456', @role='admin'	

select * from Users where password =  CONVERT(NVARCHAR(32),HashBytes('MD5', N'abcdef'), 2) 
select * from Users where password = HashBytes('MD5', 'abcdef')

SELECT HASHBYTES('MD5', '123456')
SELECT CONVERT(NVARCHAR(32),HashBytes('MD5', '123456'), 2) 

--SELECT HASHBYTES('MD5', 'abcdef')  
--ALTER TABLE Users
--ALTER  COLUMN password NVARCHAR(32) --BINARY(16)

--delete from Users

DECLARE @HashThis nvarchar(4000);  
SET @HashThis = 'abcdef' 
SELECT HASHBYTES('MD5', @HashThis);  
SELECT HASHBYTES('MD5', N'abcdef');  

--if (CONVERT(NVARCHAR(32),HashBytes('MD5', 'abcdef'), 2) = 'e80b5017098950fc58aad83c8c14978e')
--	print 'hihi'


