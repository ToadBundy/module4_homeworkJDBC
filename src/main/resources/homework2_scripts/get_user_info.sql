WITH temp_table AS(
    SELECT "user".id AS user_id, name, "user".created_at AS UserCreatedAt, MIN(post.created_at) as FirstPost
        from "user" left join post
        on post.user_id = "user".id
        group by "user".id
)
    SELECT temp_table.user_id, name, UserCreatedAt, FirstPost, COUNT(comment.id) AS comments
        FROM temp_table LEFT JOIN comment ON temp_table.user_id = comment.user_id
        GROUP BY temp_table.user_id, name, UserCreatedAt,FirstPost;