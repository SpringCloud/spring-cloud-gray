import request from '@/utils/request'

export function fetchList(query) {
  const tempData = Object.assign({}, query)
  tempData.page = query.page - 1
  tempData.size = query.limit
  delete tempData['limit']
  return request({
    url: '/gray/instance/page',
    method: 'get',
    params: tempData
  })
}

export function createInstance(data) {
  return request({
    url: '/gray/instance/',
    method: 'post',
    data
  })
}

export function updateInstance(data) {
  return request({
    url: '/gray/instance/',
    method: 'post',
    data
  })
}

export function deleteInstance(instanceId) {
  return request({
    url: '/gray/instance/' + instanceId,
    method: 'delete'
  })
}
