import request from '@/utils/request'

export function getListPage (parameter) {
  return request({
    url: '/${tablePd.mod_name}/${tablePd.bus_name}/findListPage',
    method: 'post',
    data: parameter
  })
}
  
export function saveOrUpdate (parameter) {
  console.log(parameter);
  return request({
    url: '/${tablePd.mod_name}/${tablePd.bus_name}/saveOrUpdate',
    method: 'post',
    data: parameter
  })
}

export function delData (ids) {
  console.log(ids);
  return request({
    url: '/${tablePd.mod_name}/${tablePd.bus_name}/del?ids='+ids,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

export function findList (parameter) {
  return request({
    url: '/${tablePd.mod_name}/${tablePd.bus_name}/findList',
    method: 'post',
    data: parameter
  })
}




