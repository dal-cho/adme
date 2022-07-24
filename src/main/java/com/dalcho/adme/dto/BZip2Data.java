package com.dalcho.adme.dto;

import com.dalcho.adme.utils.videoUtils.CompressUtils;
import lombok.NoArgsConstructor;
import lombok.val;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.Serializable;

@NoArgsConstructor
public class BZip2Data implements Serializable {

    private static final long serialVersionUID = -4805556024192461766L;
    byte[] data;
    byte[] bzip2;

    /**
     * 팩토리 메소드
     */
    public static BZip2Data of ( byte[] bytes ) {
        val bZip2Data = new BZip2Data();
        bZip2Data.data = bytes ;
        return bZip2Data ;
    }

    // ResultSet.getBytes(int) 로 취득된 값이 이 constructor 으로 설정된다
    BZip2Data( byte[] bytes ) {
        if ( data == null ) {
            data = CompressUtils.decompress( bytes );
        }
    }

    // PreparedStatement.setBytes(int, bytes)에 설정하는 값이 메소드로부터 취득된다
    byte[] getValue() {
        if ( bzip2 == null ) {
            bzip2 = CompressUtils.compress( data );
        }
        return  bzip2 ;
    }

    public String toBase64 () {
        return Base64.encodeBase64String( data );
    }
}
