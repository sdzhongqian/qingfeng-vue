import axios from 'axios'
import storage from 'store'
import store from '@/store'
import router from '@/router'
import notification from 'ant-design-vue/es/notification'
import { VueAxios } from './axios'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import { Modal } from 'ant-design-vue';

// 创建 axios 实例
const request = axios.create({
  // API 请求的默认前缀
  baseURL: process.env.VUE_APP_API_BASE_URL,
  timeout: 6000, // 请求超时时间
})

// 异常拦截处理器
const errorHandler = (error) => {
  console.log('异常拦截处理器')
  if (error.response) {
    const data = error.response.data
    // 从 localstorage 获取 token
    if (error.response.status === 403) {
      notification.error({
        message: 'Forbidden',
        description: data.message,
      })
    }
    if (error.response.status === 401 && !(data.result && data.result.isLogin)) {
      notification.error({
        message: '登录失效',
        description: '权限验证失败或者登录失效。',
      })
    }
  }
  return Promise.reject(error)
}

// request interceptor
request.interceptors.request.use(config => {
  const token = storage.get(ACCESS_TOKEN)
  // 如果 token 存在
  // 让每个请求携带自定义 token 请根据实际情况自行修改
  if (token) {
    config.headers['Access-Token'] = token
  }
  //验证token是否有效，如果失效刷新，如果超时退出登录
  return config
}, errorHandler)

// response interceptor
request.interceptors.response.use((response) => {
  if (response.data.code == 200 || response.data.code == '') {
    return response.data
  } else if (response.data.code == 1007) {//刷新token
    //刷新token成功后，获取新的token，覆盖旧的token,然后返回到报错的页面刷新掉
    console.log('刷新token')
    // store.commit("SetToken", response.data.token);
    store.dispatch('SetToken',response.data.token);
    //就是重新调用接口获取数据
    var backoff = new Promise(resolve => {
      resolve();
    });
    return backoff.then(r => {
      return request({
        url: response.config.url,
        method: response.config.method,
        data: JSON.parse(response.config.data),
      })
    });
  } else {
    if (response.data.msg == '') {
      notification.error({
        message: '登录失效',
        description: '权限验证失败或者登录失效，正在跳转',
      })
    } else {
      notification.error({
        message: '提示',
        description: response.data.msg,
      })
    }
    //销毁所有弹框
    Modal.destroyAll();
    store.dispatch('Logout').then(() => {
      router.push({ name: "login" });
    })
  }
}, errorHandler)

const installer = {
  vm: {},
  install(Vue) {
    Vue.use(VueAxios, request)
  },
}

export default request

export {
  installer as VueAxios,
  request as axios,
}
