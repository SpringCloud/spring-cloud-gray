import request from '@/utils/request'

export function fetchList(query) {
  const tempData = Object.assign({}, query)
  tempData.page = query.page - 1
  return request({
    url: '/gray/service/authority/page',
    method: 'get',
    params: tempData
  })
}

export function create(data) {
  return request({
    url: '/gray/service/authority/',
    method: 'post',
    data
  })
}

export function deleteRecord(id) {
  return request({
    url: '/gray/service/authority/' + id,
    method: 'delete'
  })
}
