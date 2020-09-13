package cn.springcloud.gray.service.b;

//import cn.springcloud.gray.request.GrayTrackInfo;
//import cn.springcloud.gray.request.LocalStorageLifeCycle;
//import cn.springcloud.gray.request.RequestLocalStorage;

//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = ServiceBApplication.class)
public class ExecutorServiceGrayAspectTest {

//    @Autowired
//    private LocalStorageLifeCycle localStorageLifeCycle;
//
//    @Autowired
//    private ExecutorService executorService;
//    @Autowired
//    private RequestLocalStorage requestLocalStorage;
//
//
//    @Before
//    public void before() {
//        localStorageLifeCycle.initContext();
//    }
//
//    @After
//    public void after() {
//        localStorageLifeCycle.closeContext();
//    }
//
//
//    @Test
//    public void testExecute() {
//        requestLocalStorage.setGrayTrackInfo(new GrayTrackInfo());
//        executorService.execute(() -> {
//            System.out.println("323fd");
//        });
//    }

//    @Test
//    public void testSubmit() {
//        executorService.submit(() -> {
//            System.out.println("323fd");
//        });
//    }
//
//    @Test
//    public void testSubmit2() {
//        executorService.submit(() -> {
//            System.out.println("323fd");
//        }, "");
//    }
//
//    @Test
//    public void testSubmit3() {
//        executorService.submit(() -> {
//            System.out.println("323fd");
//            return "";
//        });
//    }
//
//
//    @Test
//    public void testInvokeAll() throws InterruptedException {
//        executorService.invokeAll(Arrays.asList(() -> {
//            System.out.println("323fd");
//            return "";
//        }, () -> {
//            System.out.println("323fd");
//            return "";
//        }));
//    }
//
//    @Test
//    public void testInvokeAll2() throws InterruptedException {
//        executorService.invokeAll(Arrays.asList(() -> {
//            System.out.println("323fd");
//            return "";
//        }, () -> {
//            System.out.println("323fd");
//            return "";
//        }), 10, TimeUnit.SECONDS);
//    }
//
//
//    @Test
//    public void testInvokeAny() throws InterruptedException, ExecutionException {
//        executorService.invokeAny(Arrays.asList(() -> {
//            System.out.println("323fd");
//            return "";
//        }, () -> {
//            System.out.println("323fd");
//            return "";
//        }));
//    }
//
//    @Test
//    public void testInvokeAny2() throws InterruptedException, ExecutionException, TimeoutException {
//        executorService.invokeAny(Arrays.asList(() -> {
//            System.out.println("323fd");
//            return "";
//        }, () -> {
//            System.out.println("323fd");
//            return "";
//        }), 10, TimeUnit.SECONDS);
//    }

}
