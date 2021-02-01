import request from '@/utils/request'
export function getListPage (parameter) {
  return request({
    url: '/monitor/userOnline/findListPage',
    method: 'post',
    data: parameter
  })
}
  
export function retreatData (ids) {
  return request({
    url: '/monitor/userOnline/kickUserOffline?ids='+ids,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}
