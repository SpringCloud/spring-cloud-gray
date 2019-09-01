import request from '@/utils/request'

export function fetchList(query) {
  const tempData = Object.assign({}, query)
  tempData.page = query.page - 1
  return request({
    url: '/gray/service/owner/page',
    method: 'get',
    params: tempData
  })
}

export function update(data) {
  return request({
    url: '/gray/service/owner/',
    method: 'put',
    data
  })
}
