import request from '@/utils/request'

export function fetchList(query) {
  const tempData = Object.assign({}, query)
  tempData.page = query.page - 1
  tempData.size = query.limit
  delete tempData['limit']
  return request({
    url: '/gray/policy/page',
    method: 'get',
    params: tempData
  })
}

export function createPolicy(data) {
  return request({
    url: '/gray/policy/',
    method: 'post',
    data
  })
}

export function updatePolicy(data) {
  return request({
    url: '/gray/policy/',
    method: 'post',
    data
  })
}

export function deletePolicy(id) {
  return request({
    url: '/gray/policy/' + id,
    method: 'delete'
  })
}
