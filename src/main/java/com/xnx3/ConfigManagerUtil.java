package com.xnx3;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.io.IOUtils;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

/**
 * 读取xml配置文件的值
 * <br/><b/>需</b>
 * <br/><i>commons-configuration-1.7.jar</i>
 * <br/><i>commons-collections-3.2.1.jar</i>
 * <br/><i>commons-io-1.3.2.jar</i>
 * <br/><i>commons-lang-2.5.jar</i>
 * <br/><i>commons-logging-1.2.jar</i>
 * @author 管雷鸣
 */
public class ConfigManagerUtil {
    private static HashMap hashMap = new HashMap();
    FileConfiguration config;

    private ConfigManagerUtil(String configFileName) {
        try {
            this.config = null;
            if (configFileName.toLowerCase().endsWith("xml")) {
                this.config = new XMLConfiguration(configFileName);
            } else if (configFileName.toLowerCase().endsWith("properties")) {
                this.config = new PropertiesConfiguration(configFileName);
            }
            this.config.setReloadingStrategy(new FileChangedReloadingStrategy());
            hashMap.put(configFileName, this);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    /**
     * @param configFileName xml文件的文件名，如： xnx3Config.xml ，此文件在class的根目录
     * @return
     */
    public static ConfigManagerUtil getSingleton(String configFileName) {
        if (hashMap.get(configFileName) == null) {
            hashMap.put(configFileName, new ConfigManagerUtil(configFileName));
        }
        return (ConfigManagerUtil) hashMap.get(configFileName);
    }
    
    public FileConfiguration getFileConfiguration(){
    	return this.config;
    }
    
    /**
     * 获取某个值 
     * @param path
     * @return
     * @deprecated
     */
    public String selectValue(String path) {
        return this.config.getString(path);
    }
    
    /**
     * 获取某个值
     * @param path xml配置文件的节点路径，如sms.uid
     * @return 值
     */
    public String getValue(String path) {
        return this.config.getString(path);
    }
   
    public List getList(String name){
    	return this.config.getList(name);
    }

    public void setValue(String path, String value) {
        this.config.setProperty(path, value);
    }

    public String[] selectValues(String path) {
        return this.config.getStringArray(path);
    }

    public void reload() {
        this.config.reload();
    }

    public String toFileContent()  throws ConfigurationException {
        StringWriter stringWriter = new StringWriter();
        this.config.save(stringWriter);
        String returnValue = stringWriter.toString();
        IOUtils.closeQuietly(stringWriter);
        return returnValue;
    }

    public String getFilePath() {
        return this.config.getFile().getAbsolutePath();
    }

    public void save()
            throws ConfigurationException {
        this.config.save();
    }

    public static void main(String[] args) {
    	ConfigManagerUtil.getSingleton("xnx3Config.xml").getValue("mail.host");
    }
}
