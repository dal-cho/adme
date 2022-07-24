package com.dalcho.adme.utils.videoUtils;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
public class CompressUtils {
    /**
     * 입력한 바이트 배열을 BZip2로 압축해 돌려줍니다.
     *
     * @param input
     * @return
     */
    public static byte[] compress ( byte[] input ) {
        ByteArrayOutputStream ref = null ;

        try (val bais = new ByteArrayInputStream( input );
             val baos = new ByteArrayOutputStream ( input.length );
             val bzip2cos = new BZip2CompressorOutputStream( baos )) {
            IOUtils.copy ( bais , bzip2cos );
            ref = baos ;
        } catch ( IOException e ) {
            log.error ( "failed to encode.", e );
            throw new RuntimeException(e);
        }

        return ref.toByteArray();
    }

    /**
     * 입력한 바이트 배열을 BZip2로 전개해 돌려줍니다.
     */
    public static byte[] decompress ( byte[] input ) {
        ByteArrayOutputStream ref = null ;

        try (val bais = new ByteArrayInputStream ( input );
             val bzip2cis = new BZip2CompressorInputStream( bais );
             val baos = new ByteArrayOutputStream()) {
            IOUtils.copy( bzip2cis, baos );
            ref = baos ;
        } catch ( IOException  e ) {
            log.error ( "failed to decode.", e );
            throw new RuntimeException(e);
        }

        return ref.toByteArray();
    }
}
