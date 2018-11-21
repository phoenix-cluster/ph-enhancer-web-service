package org.ncpsb.phoenixcluster.enhancer.webservice.utils;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TableNameTest {

    @Test
    public void getSpectrumTableName() {
    }

    @Test
    public void getProjectId() {
        Assert.assertEquals("PXD000021" ,TableName.getProjectId("PXD000021;1233213.mgf;spectrum=1"));
        Assert.assertEquals("PRD000021" ,TableName.getProjectId("PRD000021;1233213.mgf;spectrum=1"));
    }
}