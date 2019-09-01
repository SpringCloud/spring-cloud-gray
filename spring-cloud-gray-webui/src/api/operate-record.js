import request from '@/utils/request'

export function fetchList(query) {
  const tempData = Object.assign({}, query)
  tempData.page = query.page - 1
  return request({
    url: '/gray/operate/record/page',
    method: 'get',
    params: tempData
  })
}
