import axios from "axios";

const myAxios = axios.create({
    baseURL: 'http://localhost:8080'
});
// 携带cookie
myAxios.defaults.withCredentials = true;
// 添加请求拦截器
myAxios.interceptors.request.use(function (config) {
    return config;
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
});

// 添加响应拦截器
myAxios.interceptors.response.use(function (response) {
    console.log('我收到你的响应了',response);
    // 对响应数据做点什么
    if (response?.data?.code === 40100) {
        const redirectUrl = window.location.href;
        window.location.href = `/user/login?redirect=${redirectUrl}`;
    }
    return response.data;
}, function (error) {
    // 对响应错误做点什么
    return Promise.reject(error);
});

export default myAxios;