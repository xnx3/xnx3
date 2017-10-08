package com.xnx3.net;

import java.util.ArrayList;
import java.util.List;
import com.aliyun.mns.client.CloudAccount;
import com.aliyun.mns.client.CloudQueue;
import com.aliyun.mns.client.MNSClient;
import com.aliyun.mns.common.ClientException;
import com.aliyun.mns.common.ServiceException;
import com.aliyun.mns.common.utils.ServiceSettings;
import com.aliyun.mns.model.Message;
import com.aliyun.mns.model.QueueMeta;

/**
 * 阿里云 消息服务
 * <br/>需要Jar包: 
 * <br/>aliyun-sdk-mns-1.1.8.jar
 * <br/>commons-codec-1.9.jar
 * <br/>commons-lang3-3.5.jar
 * <br/>commons-logging-1.2.jar
 * <br/>httpasyncclient-4.1.2.jar
 * <br/>httpclient-4.5.2.jar
 * <br/>httpcore-4.4.5.jar
 * <br/>httpcore-nio-4.4.5.jar
 * <br/>json.jar
 * <br/>log4j-1.2.16.jar
 * 
 * @author 管雷鸣
 */
public class MNSUtil {
	String accessKeyId;
    String accessKeySecret; 
    String endpoint;
    private MNSClient mnsClient = null;
    public CloudAccount account;
	
    /**
     * 创建消息服务对象，由此创建的会从用户根目录读取相关的 .aliyun-mns.properties 配置文件
     */
    public MNSUtil(){
    	accessKeyId = ServiceSettings.getMNSAccessKeyId();
    	accessKeySecret = ServiceSettings.getMNSAccessKeySecret();
    	endpoint = ServiceSettings.getMNSAccountEndpoint();
    }
    
    /**
     * 不需要单独创建 用户目录下的.aliyun-mns.properties文件，直接将值传入即可
     * @param accessKeyId 参考https://help.aliyun.com/document_detail/34414.html?spm=5176.doc34415.6.555.Wil4YL
     * @param accessKeySecret 同上
     * @param endpoint 参考 https://help.aliyun.com/document_detail/34415.html?spm=5176.doc34414.6.556.Dhtt34
     */
    public MNSUtil(String accessKeyId, String accessKeySecret, String endpoint) {
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
		this.endpoint = endpoint;
	}
    
    /**
     * 获取当前对象内的 {@link MNSClient} , 对象内只有第一次获取时会创建，并对象内缓存。以后都会直接拿过来使用，不会第二次创建。
     * @return
     */
    public MNSClient getMNSClient(){
    	if(mnsClient == null){
    		account = new CloudAccount(this.accessKeyId, this.accessKeySecret, this.endpoint);
    		mnsClient = account.getMNSClient(); //this client need only initialize once
    	}
    	return mnsClient;
    }
    
	
    /**
     * 关闭由 {@link MNSUtil#getMNSClient()} 所创建的 {@link MNSClient}
     */
    public void close(){
    	mnsClient.close();
    }
    
    
    /**
     * 获取 {@link CloudQueue}（前提是已经创建过了）
     * @param queueName 队列名字
     * @return {@link CloudQueue}
     */
    public CloudQueue getQueue(String queueName){
    	return getMNSClient().getQueueRef(queueName);
    }
    
    
    /**
     * 创建队列
     * @param queueName 队列的QueueName属性，同一账号同一Region下， 队列名称不能重名;必须以英文字母或者数字开头，剩余名称可以是英文，数字，横划线，长度不超过256个字符
     * @param delaySeconds 队列的DelaySeconds属性，发送消息到本队列的所有消息默认将以本参数指定的秒数延后可被消费；单位为秒，有效值范围为0-604800秒，也即0到 7天。
     * @return {@link CloudQueue}
     * 			<ul>
     * 				<li>null : 创建失败</li>
     * 				<li>不为null，返回 {@link CloudQueue}对象 : 创建成功</li>
     * 			</ul>
     */
    public CloudQueue createQueue(String queueName){
    	QueueMeta qMeta = new QueueMeta();
    	qMeta.setQueueName(queueName);
        return createQueue(qMeta);
    }
   
    /**
     * 创建队列
     * @param queueName 队列的QueueName属性，同一账号同一Region下， 队列名称不能重名;必须以英文字母或者数字开头，剩余名称可以是英文，数字，横划线，长度不超过256个字符
     * @param delaySeconds 队列的DelaySeconds属性，发送消息到本队列的所有消息默认将以本参数指定的秒数延后可被消费；单位为秒，有效值范围为0-604800秒，也即0到 7天。
     * @return {@link CloudQueue}
     * 			<ul>
     * 				<li>null : 创建失败</li>
     * 				<li>不为null，返回 {@link CloudQueue}对象 : 创建成功</li>
     * 			</ul>
     */
    public CloudQueue createQueue(String queueName, long delaySeconds){
    	QueueMeta qMeta = new QueueMeta();
        qMeta.setQueueName(queueName);
        qMeta.setDelaySeconds(delaySeconds);
        qMeta.setPollingWaitSeconds(30);//use long polling when queue is empty.
        return createQueue(qMeta);
    }
    
    /**
     * 创建队列
     * @param qMeta 要创建的队列
     * @return {@link CloudQueue}
     * 			<ul>
     * 				<li>null : 创建失败</li>
     * 				<li>不为null，返回 {@link CloudQueue}对象 : 创建成功</li>
     * 			</ul>
     */
    public CloudQueue createQueue(QueueMeta qMeta){
    	try{
    		return getMNSClient().createQueue(qMeta);
        } catch (ClientException ce){
        	clientException(ce);
        } catch (ServiceException se){
        	serviceException(se);
        } catch (Exception e){
        	exception(e);
        }
    	return null;
    }
    
    /**
     * 发送消息
     * @param queueName 要发送消息的队列名字
     * @param messageBody 待发送消息正文，控制台消息发送接受的缺省编码为base64， 和Message Service 的官方SDK一致。
     * @return {@link Message}
     * 			<ul>
     * 				<li>null : 失败</li>
     * 				<li>不为null，返回 {@link Message}对象 : 成功</li>
     * 			</ul>
     */
    public Message putMessage(String queueName, String messageBody){
    	 Message message = new Message();
         message.setMessageBody(messageBody);
         return putMessage(queueName, message);
    }
    
    /**
     * 发送消息
     * @param queueName 要发送消息的队列名字
     * @param message 发送的消息对象
     * @return {@link Message}
     * 			<ul>
     * 				<li>null : 失败</li>
     * 				<li>不为null，返回 {@link Message}对象 : 成功</li>
     * 			</ul>
     */
    public Message putMessage(String queueName, Message message){
    	try{
            CloudQueue queue = getMNSClient().getQueueRef(queueName);
            Message putMsg = queue.putMessage(message);
            return putMsg;
        } catch (ClientException ce){
        	clientException(ce);
        } catch (ServiceException se){
        	serviceException(se);
        } catch (Exception e){
        	exception(e);
        }
		return null;
    }
    
    /**
     * 列出这个队列内的消息（最多每次列出15条）
     * <br/>消费后可使用 {@link #deleteMessage(String, Message)} 将其删除 
     * @param queueName 队列名字
     * @return List 可根据 list.size() 进行判断当前是否有消息
     */
    public List<Message> listMessage(String queueName){
    	List<Message> batchPopMessage = null;
		CloudQueue queue = getMNSClient().getQueueRef(queueName);// replace with your queue name
		batchPopMessage = queue.batchPopMessage(15);  
        
    	if(batchPopMessage == null){
    		//避免空指针异常
    		batchPopMessage = new ArrayList<Message>();
    	}
    	return batchPopMessage;
    }
    
    /**
     * 删除队列内的指定某条消息
     * @param queueName
     */
    public void deleteMessage(String queueName, Message message){
    	CloudQueue queue = getMNSClient().getQueueRef(queueName);// replace with your queue name
    	queue.deleteMessage(message.getReceiptHandle());
    }
    
    /**
     * 删除队列
     * @param queueName
     */
    public void deleteQueue(String queueName){
    	try
        {   //Delete Queue
            CloudQueue queue = getMNSClient().getQueueRef(queueName);
            queue.delete();
        } catch (ClientException ce){
        	clientException(ce);
        } catch (ServiceException se){
        	serviceException(se);
        } catch (Exception e){
        	exception(e);
        }
    }
    

    /**
     * 服务器返回的异常捕获
     * @param se {@link ServiceException}
     */
    private void serviceException(ServiceException se){
    	if (se.getErrorCode().equals("QueueNotExist")){
    		System.out.println("Queue is not exist.Please create before use");
    	} else if (se.getErrorCode().equals("TimeExpired")){
    		System.out.println("The request is time expired. Please check your local machine timeclock");
    	}
    	se.printStackTrace();
    }
    
    /**
     * 客户端导致的异常捕获
     * @param ce {@link ClientException}
     */
    private void clientException(ClientException ce){
    	System.out.println("Something wrong with the network connection between client and MNS service."
                 + "Please check your network and DNS availablity.");
    	ce.printStackTrace();
    }
    
    /**
     * 客户端、服务端导致的异常除外的其他异常
     * @param e {@link Exception}
     */
    private void exception(Exception e){
    	 System.out.println("Unknown exception happened!，exception:"+e.getMessage());
         e.printStackTrace();
    }
    
    /**
     * 不是实时的，测试应该有个几秒的误差延迟
     */
    public static void main(String[] args) {
    	//方式一，在当前用户目录建立 .aliyun-mns.properties 配置文件，将三个参数加入其中
    	MNSUtil mns = new MNSUtil();
    	//方式二，创建MNSUtil时，直接传入
//    	MNSUtil mns = new MNSUtil(accessKeyId, accessKeySecret, endpoint);
    	
    	
    	//队列名字
    	String queueName = "testaaaaa";
    	//创建队列
    	CloudQueue cq = mns.createQueue(queueName);
		
		//向队列中加入消息
		mns.putMessage(queueName, "哈哈1");
		mns.putMessage(queueName, "哈哈3");
		
		//获取队列
		List<Message> list = mns.listMessage(queueName);
		System.out.println("消息条数:"+list.size());
		for (int i = 0; i < list.size(); i++) {
			Message message = list.get(i);
			System.out.println(message.getMessageId()+":"+message.getMessageBody());
			mns.deleteMessage(queueName, message);	//用完消息后删除掉
		}
		
		
		//删除队列
//		mns.deleteQueue(queueName);
		mns.close();
	}
    
}
