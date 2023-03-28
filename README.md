# 智寻-伙伴匹配系统 

Wisdom-Found includes frontend and backend.The user-center is backend,yupao-frontend is fronted

技术栈：Spring Boot + MySQL + MyBatis-Plus + Redis + Knife4j + Vue + Vite + Axios + Vant UI
项目描述：智寻伙伴匹配是一款移动端软件，在创建用户时添加了标签属性，通过标签匹配给用户进行组队，相同标签的人可以一起组队学习、运动等活动。主要实现了用户管理、按标签检索用户、推荐相似用户、组队功能。
主要功能：
1. 用户登录使用Redis实现分布式Session，解决集群间登录态同步问题
2. 使用Redis缓存首页高频访问的用户信息列表，将接口响应时长从6秒缩短至2秒
3. 使用Spring Scheduler定时任务实现缓存预热，并通过分布式锁保证多机部署时定时任务不会重复执行
4. 使用Redisson分布式锁来实现操作互斥，解决了同一用户重复加入队伍、入队人数超限的问题
5. 使用 Knife4j + Swagger 自动生成后端接口文档，编写 ApiOperation 等注解补充接口注释提高开发效率
6. 使用编辑距离算法，实现了根据标签匹配最相似用户的功能
