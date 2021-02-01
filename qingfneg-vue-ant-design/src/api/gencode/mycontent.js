import request from '@/utils/request'

export function getListPage (parameter) {
  return request({
    url: '/gencode/mycontent/findListPage',
    method: 'post',
    data: parameter
  })
}
  
export function saveOrUpdate (parameter) {
  console.log(parameter);
  return request({
    url: '/gencode/mycontent/saveOrUpdate',
    method: 'post',
    data: parameter
  })
}

export function delData (ids) {
  console.log(ids);
  return request({
    url: '/gencode/mycontent/del?ids='+ids,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

export function findList (parameter) {
  return request({
    url: '/gencode/mycontent/findList',
    method: 'post',
    data: parameter
  })
}




