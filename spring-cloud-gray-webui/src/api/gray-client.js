/**
 * Created by saleson on 2019/11/26.
 */
import request from '@/utils/request'

export function getAllDefinitions(serviceId, instanceId) {
  var params = { 'serviceId': serviceId, 'instanceId': instanceId }
  return request({
    url: '/gray/client/grayList/track/allDefinitions',
    method: 'get',
    params: params
  })
}

export function getServiceAllInfos(serviceId, instanceId) {
  var params = { 'serviceId': serviceId, 'instanceId': instanceId }
  return request({
    url: '/gray/client/grayList/service/allInfos',
    method: 'get',
    params: params
  })
}
