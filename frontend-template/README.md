# Ant Design Pro

This project is initialized with [Ant Design Pro](https://pro.ant.design). Follow is the quick guide for how to use.

## Environment Prepare

Install `node_modules`:

```bash
npm install
```

or

```bash
yarn
```

## Provided Scripts

Ant Design Pro provides some useful script to help you quick start and build with web project, code style check and test.

Scripts provided in `package.json`. It's safe to modify or add additional script:

### Start project

```bash
npm start
```

### Build project

```bash
npm run build
```

### Check code style

```bash
npm run lint
```

You can also use script to auto fix some lint error:

```bash
npm run lint:fix
```

### Test code

```bash
npm test
```

## More

You can view full document on our [official website](https://pro.ant.design). And welcome any feedback in our [github](https://github.com/ant-design/ant-design-pro).

## 本地开发

如果可以当前网络环境可以访问 武汉计费的测试环境 http://10.1.115.57，可以设置代码地址，直接启动

```
export REACT_APP_API_URL=http://10.1.115.57
npm run dev
```

如果有新依赖，docker 运行前，需先 install 一下，使用 [docker-compose-yarn.yml](docker-compose-yarn.yml)

```
docker-compose -f docker-compose-yarn.yml
```
