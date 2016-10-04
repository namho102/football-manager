alter trigger update_position on Ranking
after update
as
if(UPDATE(pts))
begin 

	--select * from Ranking order by pts desc;
	with cte as (select pos, row_number()  over (order by pts desc) as rn from Ranking)
	update cte set pos = rn;
end
go

update Fixtures set home_goal = 1, away_goal = 2  where id = 1
--insert into Fixtures values (1, 'Southampton',  NULL, NULL, 'Watford', '08-13-2016', '21:00:00')
--delete from Fixtures where id = 1
select * from Ranking order by pos