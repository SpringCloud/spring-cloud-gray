package cn.springcloud.gray.event.codec;

/**
 * @author saleson
 * @date 2020-01-31 22:34
 */
public interface GrayEventCodec<IO> extends GrayEventDecoder<IO>, GrayEventEncoder<IO> {
}
