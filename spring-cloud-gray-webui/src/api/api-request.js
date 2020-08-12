import request from '@/utils/request'

export function fetchList(url, query) {
  const tempData = Object.assign({}, query)
  tempData.page = query.page - 1
  tempData.size = query.limit
  delete tempData['limit']
  return request({
    url: url,
    method: 'get',
    params: tempData
  })
}

export function createRecord(url, data) {
  return request({
    url: url,
    method: 'post',
    data
  })
}

export function updateRecord(url, data) {
  return request({
    url: url,
    method: 'post',
    data
  })
}

export function deleteRecord(url, id) {
  if (id) {
    url = url + escape(id)
  }
  return request({
    url: url,
    method: 'delete'
  })
}

export function recoverRecord(url, id) {
  if (id) {
    url = url + escape(id)
  }
  return request({
    url: url,
    method: 'patch'
  })
}

export function putData(url, data) {
  return request({
    url: url,
    method: 'put',
    data
  })
}

export function getData(url, query) {
  const tempData = Object.assign({}, query)
  return request({
    url: url,
    method: 'get',
    params: tempData
  })
}
