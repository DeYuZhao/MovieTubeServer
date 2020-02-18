# MovieTube系统后端部分说明文档 #

## 目录结构说明 ##
1. /api包下存放dto实体以及controller层的接口，这些接口是前端可以调用的api，在此处定义前后端交互的接口
2. /config包下是一些全局的配置
3. /support/exception包下定义了一些自定义的异常，以及全局的异常管理器，在开发过程中最好只在需要的地方打日志，其他时候抛异常给最上层的异常管理器统一进行日志打印
4. /support/jwt包下是JSON Web Token相关的类
5. /support/response包下定义了前后端统一的交互对象，RestApiResponse是后端给前端的统一返回体，使用RestApiResponseUtil来创建，如果需要返回异常情况，需要传输相应的HttpStatus状态码，便于前端判断
6. /support/security包下是一些安全配置
7. /utils/ObjectUtil类实现了对象转换的方法，用于将数据层实体(po)转换为上层实体(dto)
  + 约定：dto在controller层和service层之间传递，service和dao层之间传递po
8. 有不需要提交的文件，写在.gitignore里

## 开发说明 ##
1. 新增数据库实体(po)时，在resource/mybatis/config/mybatis-config.xml中新增实体的alias name，然后在mapper下新增dao层的xml，编写sql语句
2. 数据库表结构发生变化时，需要导出相应的ddl、dml语句，保存在resource/sql下(mysqldump导出或者navicat或者其他数据库连接工具导出)
3. 在开发环境时，applicaiton.yaml中spring.profiles.active选择dev，项目完成后部署时选择prod
4. 在开发环境时，启动项目后可以在localhost:45032/MovieTubeServer/api处看见后端提供的接口，方便前端了解后端接口，以及方便后端测试接口，也可以使用postman等工具进行测试，这里除了login、logout接口以外的其他接口都需要经过鉴权才能调用，如果需要测试其他接口，可以在/config/WebSecurityConfig.java类中对cors进行配置
5. 在开发环境时，开放/api路径查看接口文档，部署时需要关闭
6. 前后端均fork一份到自己的本地，pull的时候从主仓库拉，push的时候推到自己的仓库，然后发MR给主仓库
7. controller层路由统一加一个前缀/api，如：/api/module/method

## 打包部署 ##
1. 打包脚本后期再完善

## 访问地址 ##
1. 前端地址：http://172.19.241.56:8000/
2. 代码质量检测前端地址：http://172.19.241.56:9002/project/issues?id=movieTubeFront&resolved=false
3. 代码质量检测后端地址：http://172.19.241.56:9002/project/issues?id=movieTubeServer&resolved=false