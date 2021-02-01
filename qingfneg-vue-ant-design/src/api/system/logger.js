import request from '@/utils/request'
export function getListPage (parameter) {
  console.log(parameter);
  return request({
    url: '/system/logger/findListPage',
    method: 'post',
    data: parameter
  })
}

export function del (ids) {
  return request({
    url: '/system/logger/del?ids='+ids,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}
