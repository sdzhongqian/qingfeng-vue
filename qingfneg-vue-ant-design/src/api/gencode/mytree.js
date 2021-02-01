import request from '@/utils/request'

export function getListPage (parameter) {
  return request({
    url: '/gencode/mytree/findListPage',
    method: 'post',
    data: parameter
  })
}
  
export function saveOrUpdate (parameter) {
  console.log(parameter);
  return request({
    url: '/gencode/mytree/saveOrUpdate',
    method: 'post',
    data: parameter
  })
}

export function delData (ids) {
  console.log(ids);
  return request({
    url: '/gencode/mytree/del?ids='+ids,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

export function findList (parameter) {
  return request({
    url: '/gencode/mytree/findList',
    method: 'post',
    data: parameter
  })
}
 



