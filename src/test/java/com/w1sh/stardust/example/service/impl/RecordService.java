package com.w1sh.stardust.example.service.impl;

import com.w1sh.stardust.annotation.Inject;
import com.w1sh.stardust.annotation.Provide;

@Provide
public record RecordService() {

    public record InnerRecord() {}

    public record InnerRecord2(String name) {
        public InnerRecord2(){
            this("name");
        }
    }

    public record InnerRecord3(String name) {
        @Inject
        public InnerRecord3(Integer integer){
            this(integer.toString());
        }
        @Inject
        public InnerRecord3(){
            this("test1");
        }
    }
}
