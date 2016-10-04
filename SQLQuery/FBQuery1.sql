alter trigger update_ranking on Fixtures
after update
as
if(UPDATE(home_goal)) 
begin
	declare @home nchar(50)
	declare @away nchar(50)
	declare @hg int
	declare @ag int
	select @home=home, @away=away, @hg=home_goal, @ag=away_goal  from inserted

	--update played game
	update Ranking set pl = pl + 1 where team = @home or team = @away

	--update home scores
	update Ranking set f = f + @hg, a = a + @ag where team = @home
	--update away scores
	update Ranking set f = f + @ag, a = a + @hg where team = @away

	declare @gd int
	set @gd = @hg - @ag

	--update goal difference	
	update Ranking set gd = gd + @gd where team = @home
	update Ranking set gd = gd - @gd where team = @away
	
	--win, lose or draw
	if(@gd > 0)
		begin
			print 'home win'
			update Ranking set pts = pts + 3, w = w + 1 where team = @home
			update Ranking set l = l + 1 where team = @away
		end	
	else if(@gd = 0)
		begin
			print 'tie'
			update Ranking set pts = pts + 1, d = d + 1 where team = @home or team = @away
		end
	else
		begin
			print 'away win'
			update Ranking set l = l + 1 where team = @home
			update Ranking set pts = pts + 3, w = w + 1 where team = @away
		end	
		

end	
go

--update Fixtures set home_goal = 3, away_goal = 0  where id = 1
--insert into Fixtures values (1, 'Southampton',  NULL, NULL, 'Watford', '08-13-2016', '21:00:00')
--delete from Fixtures where id = 1
