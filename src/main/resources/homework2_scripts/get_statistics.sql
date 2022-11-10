CREATE TEMPORARY TABLE stats (user_count INTEGER, post_count INTEGER, comment_count INTEGER, like_count INTEGER);
INSERT INTO stats(user_count, post_count, comment_count, like_count)
        VALUES ((SELECT COUNT(*) FROM "user"),
	            (SELECT COUNT(*) FROM post),
	            (SELECT COUNT(*) FROM comment),
	            (SELECT COUNT(*) FROM "like"));