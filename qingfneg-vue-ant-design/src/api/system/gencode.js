import request from '@/utils/request'

//查询表列表
export function getTableListPage (parameter) {
  return request({
    url: '/system/gencode/findTableListPage',
    method: 'post',
    data: parameter
  })
}

//执行表的保存
export function save (parameter) {
  return request({
    url: '/system/gencode/save',
    method: 'post',
    data: parameter
  })
}

//查询分页列表
export function getListPage (parameter) {
  return request({
    url: '/system/gencode/findListPage',
    method: 'post',
    data: parameter
  })
}
  
//查询详情
export function findInfo (id) {
  return request({
    url: '/system/gencode/findInfo?id='+id,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

//更新
export function update (parameter) {
  return request({
    url: '/system/gencode/update',
    method: 'post',
    data: parameter
  })
}

//执行删除
export function delData (ids) {
  return request({
    url: '/system/gencode/del?ids='+ids,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}

//代码生成gencode
export function gencode(id) {
  return request({
    url: '/system/gencode/gencode?id='+id,
    method: 'get',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8',
    }
  })
}






