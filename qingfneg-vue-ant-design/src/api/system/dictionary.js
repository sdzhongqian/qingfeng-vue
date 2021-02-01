import request from '@/utils/request'
export function getListPage (parameter) {
  console.log(parameter);
  return request({
    url: '/system/dictionary/findListPage',
    method: 'post',
    data: parameter
  })
}
  
export function saveOrUpdate (parameter) {
  console.log(parameter);
  return request({
    url: '/system/dictionary/saveOrUpdate',
    method: 'post',
    data: parameter
  })
}

export function delData (ids) {
  console.log(ids);
  return request({
    url: '/system/dictionary/del?ids='+ids,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

export function updateStatus(id,status) {
  return request({
    url: '/system/dictionary/updateStatus',
    method: 'post',
    data: {
      id,
      status
    }
  })
}

export function findDictionaryList (parameter) {
  return request({
    url: '/system/dictionary/findList',
    method: 'post',
    data: parameter
  })
}




