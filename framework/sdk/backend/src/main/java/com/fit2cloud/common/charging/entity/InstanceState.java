package com.fit2cloud.common.charging.entity;

import com.fit2cloud.common.charging.constants.BillingGranularityConstants;
import com.fit2cloud.common.provider.util.CommonUtil;
import lombok.Data;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * {@code @Author:张少虎}
 * {@code @Date: 2023/5/22  14:50}
 * {@code @Version 1.0}
 * {@code @注释: }
 */
public class InstanceState {

    /**
     * 实例状态
     */
    private final List<State> stateList;
    /**
     * 实例进制
     */
    private final int radix = 10;

    private int mark;

    public List<State> getStateList() {
        return stateList;
    }

    public static void main(String[] args) {
        Time of = Time.of(01, 2, 2);
        System.out.println(of.toString());
        InstanceState a = new InstanceState("vuol60a6vm3433h0f5a2cwkiky09pybjeidd8yj387fwwplr7tbdh61pnbfcwtmgtond0bq3zc4qnhmpc6ug317n2a6h5kjw49supfh26xu564pct2rhnayw1v3z0yeoyrk2tlxdiuzn0wqq6519h9hwtafvd4ftjyomd416lyvh4me1m8mywb2dwlo2pojm50fn9w2ez7pmh8who1raj9edt1siqiftvc7ts7j4ha6e69w4feobgpih3y0g7to1mog00ix54a04fb5tmuic949p6abkssyn7175cf3ccy63y6dq8odzijx20z7w26ryyv85a2ov0cqerbns3eiyiti8nyt3ltwpk1cngdti6egryd5k69sdotd29ijxd6vqnypzxak1ny5usn4p4v8lqkqx2xjwwueoh1cpubpewkratxxpk23nrq8ekh8qmtq5f6kogujxzfwb19jvix9kcyh38uxjhbacecvbed3tp9rkxbb19hoba1zv8w86hmwda2eu6tcje0q95wqz8zunkickm8n9uo3gneax4jvbpzf1rjzk592224gycd20ekvfpqaf6kknhh7op5q2jinzt2c0e1ily56q2qmhx4ehjd45ysp9qogo3b54zkujv396kjxaq56jw6awalzublk74rosj9q5kcxdp86xcc9atg5zvi2k19693gk3rb7xcpmvebcup94hm2l0qtd2cpp70vp6x9tcg3h5jimu4qfs1x6rl4bzyqk1tfgt2t5pebl1b80r0eai95nz4iulz1wo4cnlezkdjt33sv0uc129393earinfpyp4gkqqwyeeeunpcrdecjsdhmk6jvgfnc88xw1kp7a8k803obba9ovltogpg5sz914mdcovdzlvulow9h9a0h1h9f21aot3wbasvon0bzjarvksccaqhcp1cqsymacyv1tzpn9b1xphhzwxcq82gpxqhkfb0352lv80vrueqerut1qzuw2f5q5bakvmwzf90vi7falrnqu146e8h13aey5ankp48sb3yx6ygkxum6f5ukag9nrnv057c2zz5tcrmi95598tt4tr5i5c9c6w3arjos4fzhhaq38ws3g1ljqvn91chhk3y4j7ho3cq7gmkdx0d4nfb1a5eucky6n6d0zq4go3fnbh6ossynrn50qcg0ciicqzejmo3zayz35q4mpd1nu42qgax8t9a70sebivm2ue5yo5dm7yxot7h5fmpszjqb0k30l1jj85heid4gtwul0o0hcl5inoidljeiwe9hmuhdzphswhzkquor46u11wl3s0ri7him1ao1q5www4tijzfpfvarvyhks5upu3cvwombjcz09wx9wf2myimbnhljlo2s0qw49f1gk119b9sgo8t43qslaku5bo3wjoinxmvr4nhutxnkq558mgz7vrggnodpqh1s8wprqo97525lxa8at8gkhndmeggllbd4jqkycppcsoqkynotdyu2c0xy6lkt6c2gmvl01dbmv2wehb5wgmbievfs8c530y23a3w435stm2i5vm83b4jmhtkh1jofnmiw4mvilrzlbb09dgn8o7t91vh8i4avj8m7wglleenyh4z29upk4nubc2g7u6e9xhw6rauxaxt86n170thq2g8c6nm3kqt55i0q9jil15ty53ho1bvr1e3qaim4qggx51o1skv6h63a83q18sv0b9swbsrzksknnkdgiumiyxplxjsw6caz6uy0qiynlwlxsavzbiddkwvb3foxingopyup01s5og2elspnulhjmnzepr2rz1jz2l8e3v0auusfx132x27kr3jh5dud1qbixlwf8cwt8td5xzd7a9vof1rhp4yenw61xtf7nbkosz2ran6lrdg5y6b8a52a6fdgoahvj6mwaq3z10je7tcq0e6x3iwfc8z0e8gokhs6zf56vz2u6r1ipfn825az7m9i0umxo5jjy5v6gy2nzrrw87b0qgu8lktoeuuncubpqje1nltobrd3qbvuo20nc5nc4uudukj7i60yj1heyv25f3yud4m7rcowg8y1tzzafrux10lwhni62iggkffff5t7sq9gcpm64e1x87ver4q6pae4dgskqwwxl6yiluaqpzabqdag37quqlow9xxwuumbmcv9od14xa0i81893tivotjbjuj0f4ngp5zwjlh980y4r97nx1krfvjz1zfoeccsw9xk9mbyqztjcqm2ye6nuar7zpmc8ucjh0axajxcwlf38oxqteuc1kohc6u3qubcsrsv5t5h45kml5ytjh29o0prw6gycki6odlddgwnoaj940v6h08bfhhya3s2sduukj0f8yugu9ij0g3c0n8ay1etx1gjb7t53g5m1nqcvvdf4ivolnr6yid939nf6lk37m63yog60jhc864sfj98z5mxthfrmv710rd08es80ijfj1pf7pnhpndg83lj3pwvewjlf8419cylb1l0h60hurrjmhfvgns86kal9nszh5o9ibdxo11j4spww920yci920usj5gm14ko7eiv385f6kevpe8jembss8h572yt1djx42mjrjqcvchfksxnn7hpginfh4rs4d44j4yydimlrpxahyyld0ct3z0zwoitt302o2czn7zutwj1g7s6kayuncfn0jdd5uaahrcrjbkk3a67m0mfsd315r7n3vhpvlh9q6vy9b2wo6se5xlh4se7mhbay9qpa9bo14h00bhh80ljgm0ojzl2obgp8on5x0igzh6cw4tyoixnn6sh391vvpg0ger7tf5t9bxh9vkffy2rlrtwce1xar41rch0udlo4avaaaz2r6wgqnh505gu1rkm04vp8j3l5d6ci8h38v14xpeaws3q076kc6a5r2fmeumwb3tzcbg1k6wvpdc8flh9q0bzbgkziss63987a16c1fx5d4tbivf1n98ylv564fi87f1ewgnyptck3pyxz1u89dqnff7zkqfqsbr7inuifqhi0lq2gw8p4gp4npql57l3e07xzauj1u5dubqct0s1k415icc7t8uijzdfsvoazem6ohwkocm7hyffq8ky9vfu3xxntf23mrdkgcfnn82khncj4ade3bvmrst1kq4r4v5365vro4a565j566k3eafm62u732qigvccq92tmrw53ht1ehhmh19mlapca6v2g67r18c8oz3679gaonzk0rpjecrnxnjoctxunyfguhrtii4t26j5tsoppudl427dqrouw0jn7gj81xbfce4i3abwlvp127pmnhftkjry0i6z9rcw0pcvi6kzauz8kkfy22k773hlm4bu7ws9w03n3ct24n6yxcpigoc35dh32ggyf1t762r2wly1sz88oxvcc3z9qdxkvazjw64ektc4l5yrlxbckaucwkpcggfmqsm2njc1dl7g3z89no8h2vyokqpx34d2yum5tguud8c1rii29rpljzb068h0nczo5b5piucma3kppr6j5ust9vfct285t61nwirr38w0akhcffmtxn3p60f8h0em2wrifdrefhd4wmxmwawrpcqjpxwcci5wfx2c3scm9ftps8bejzczl78ogwkudk30thnaqsybw9dzbxqybriwlsbahw9qh3jgkh0nboxe1hrn24u1nzpocd40hi92u8bofb3vyk7xp3aeytnd6pghv6z8ndvrv7i9zloc0onx9fwkl0ijvooh0j7mowgat9j55d7f2pf5063uquv8lisbwd43lc6f1fcrdzqkbi1bj9usbud67vjm2auyenf4equ3alb4v07h58d5d14zuqszrm5np8szh9ws25mqc7lsyrf6ou0az2cug4f4xaqmq9pm53jldl0gex45831e6lvw39o1o3ro89nwczynnxmnfbt0nuo7l6luo4g73e9akbr97hdfxgo2h28m8ri23pze79uwm8qowj5m2jjbtdj65crfcx10a5gwhmvheklt8j5dr3pbt6n98ir0ip7snqk5g00x6qfzaj90eyk2hhjxmynhvro1k9ym5ftbgpm9ucy0qy3p4rxczh41b6e9rtfs10z8v7wbmkouvglp219lmzipf9z3ypvrt3e0d18om3mxrnneov5gwqswgus5im7gra8284hd9od90388sqw0r7ntgn4uao3lgvao6d9swm72oqzor5soooxk0tqtknb9birv3lm2w9kmmwrnvzx0y5e33r22u8d5p8wk7ssn1yyvprlbihremfof0eh99ez3exw4y00wczd5e4bcelixriy8wtxuh4q64lvocf8ff6ie1zm6odg8cw6i2cvv1irhbcvmhk8j1n8855bi8o7s5ymcg9mhuh6fq5mjrmmx8fhjy876c3u84seh7ljweifsp04ndsp5ewr4tmg0p3j0691xbo1qo2wvyzjdhym3nhasf4rk8br7627di7eivwrahovv5h25098oz2iajyyewd7fijlr5ndtarfedfxljz6cpn178ur5m4hx3jyuao8kfp8myjgezjlryydjn6vori4dj8v4b9sbcqj9jcr9qu8rbfucygflqx96n59d4sw7xgwko5bxj6kxsfwa1pug0nh4e7iciq2ormv267kjo113t48mws9i7zr3po6xhihzpo5yk2lv6be8ezch9uld08g576uv8hcmx1b5iu4ellg9cq4o3sszyvhw5505prvzbqfw6d48b4nkd7rs74xnrz7e64fgwmljx86o7sjyk732xlqmp32vyubjk56hgfi80cow4x3kvjn52w0xpv5bdrccpigzc7hkuvonadlbbsvvxm6t5ij5545cvg65cxtx3zeyvnvdcmtikxukuw21tqbcschy97urq3cef30p0gigfwcmtd76lzsgzcz22gbh09zbtv0hc5zp7jid3qoxp21130w430n1saughz1u06fu0zc310lgt2nocvnqhpm4egu5hva9xgmjnwryrppdsprbbuueql5cjoeu3zen52c6osw4cbqjjxxseeswt9i10osxk6bejdkx3asldbsvwftgnrzuv6eqwfxk675htbxf1iu4wpsmb5fpf48b7idygpbe70vhlo1p6amtw0bugrhvbw2bmffep9hy05huyc14cideo28ni19clqyfnh2588czviyhozm3fe91gt4gvtx6oqym9h9wwtfd0onny5ob9iopgcsuvrdzs0egknb19damcp8bf5i0zm7k92tl6htlw68tfnn07rnqaaejyvwx10qmtkwdkmzyl7945er1s35uesc4yepqsntn0czi1i6e1gkjjdi40hknfrlktyj6ajw4j5o2hryav31tc0nfmj6uqmm9me15mpsq9lw10eq1qywrkrrlsq3nah8gknjc56syi8exum90jpjqd5yrqi587d2mxdgbhqd1bt81q2vope91r2ul8eji2y96jsu34r7qq003399r4efobratdsewojvn5ks61e6l48t5pcw55vgyxtjygfvegg3k15ahib0zbvvxmyvqqb1l32ckp62on64ifewyrqru792byvnmdeij6mzrjgrtnmalsdi9pnzlghh33j1gptanx189wmubqwuxuoh6jtxb4e9ruxe0lzu24dob2wi6hex4ffjnbjfry7zqxuugx753gw6dp1038rzt96ui6atqoyuky5j5kmq64cvqm1nqu4aerjgchvy3t1u9xnxi6mvcu37c5glifnxm3aho5ds2zv9vv29rr0jdgl2sg55mvkpdzjcexdosw56voc391h6yi7m3h6dyvcp2a56awv9r5arvo6j43bc7rujujcfo3y2nzskbii9r6s00q2mifj9pvhh97yjm4qgcs97fs8dsjwcl2qp0vhk8r77c2x88pgjs6nj8n421w44mc8sv5q8d8khtvnr59duamy0p7u36a60u12u6ry1bsqu6xwtwaiwsq7fv423lbv71amnqe5sj3qs8gz7fi6f28f8dkl3zg6yc2vhm1hwtwalbhfi0ubwhgoz1x2vee1ixiv6qzko1561ydv58l3xf10p52mkk4dhfu4jelkqa1xhubxvtxvk5ifycvphe8onk330rh1p0xokdub8w6u4qvlc04abszxa2p41t1rtek3bpfvbg7cz5toixm5y1h4d1y1euiqrwpav3cpjxo7g89k7e619xkof1n96hoo51n35jru8lflq8iiz7v7xgqa48h1jqz0rkh10stwxfm9jwmpa495doajgmlexwlrqmg9vwiaqc94cxlrq4ptyhy78cbtssgpjy0zawvlgkggyx2rwzoa7yrhsx42gsu34ci3t9tyeqmoififokbzka74lc2soflg3xxxbkx72d44gfrumgmpqyfmpq7woo2dkc4d5br2byig1ykdk0byvae1aqzpa9ee9yxskx3z35czx4fi68b1pylhjern6tzmdaomsepv5wvzdc5b0efuccffnxhzw5atmjo08qb8awl9q3qmuxthrs15hrggtk63c3avj2sz4094us868j5gwdt5hd9c5gerr5b65ti81z8hg1mdzzx515icawtlliuimyprqx5ovxa17jeiyw2prj8d5yx27ctx05bq3fn2wrfokygkegvleljbyn074yucr8b43quw6eiy8dwmt5tbt8mqle5nraq5a2osfc60iu7i1ta7dwh728s54geqqmkkcml5layaeso0b3qoalnur0gtofvop85vz4s97l8ayyhd1os50awqnauf5mohqba4um5zbg7sjkbwibdy74jj5tlg9mcpi8vj8htdjpgf9s7ex2svw7ar58lt8dysppzf7xg9m0zh1pzi1avycr4wola8jtxs6idmub5r8b698mkwevrc61if43an0v330zrajhpkvfsoh3adh49zxqttybzzljc337h96exzmnafudcvmo6pnhju8gaxbj3rzc9wzks4l5clf249enhgjul5e8131p85ulfdlc6scnvgjbnx2h1b7smz7yono7ca1ou5r5g6mp0dro9g77rnxh9v9r14xqn9kjuyhrpoy8rnhhyw8n1lahl6zimc8lerdmbf3xu1tysmxhdv6q23ow1bhcnb8metwf4or1jcx4b8a3tpg36hv4xbfdq4g4vcx8ozyt1t7z8uq6084ud80sym6229l452hvxkps1s96tyhe06rlk2fp63vdwbpby8dz24a5k2tkqmbnfj3qu8tx32bgi9b7vgkx30g9mi227fzc88srt1dsg4ffns266vjh09fk39u3zutiisshqgifmo3q8v14ufm7zmjfqo4u9rmueal9vkhhjzm6mb6kpqelf3wm7tx9xqkviw4v2dw991h5udllmbrghcuevl8bsjdzsywy0m7iw2sww6mukd82sfjypwc8gcfabzpv6i9p4ilfcixua2vb0zfkoec0l6mrb8nzijchyil1rv8tph0spantp4yd60w2jyinkzev6k1xfc4ch7mtjevqtbvhn3zpqnuvmvotqty1gawbd3kgjgvjp03ldy1wd416ls09iyr2mz0i6mrdhvqpy2r6q4anw35j0tka5udr3zzi11tc2r7fibnhlprtaghl7k5tepv8pd95qnq32q4y1g1mx6uqz54454kahbo9fvuxasoy6r03wemauzm9a7d7zfflp7ghz7e92fjk03zw20x714780289xa5q6vqzndrh1ed908nmjimg9ztfox6b808gvl481jjc8035z5ixu5qac3ahshqckelqpij3syortws6psu9kuk12mz6emhlm17bwrnsatn92oskim8p2frvzkejgxg4kqnxqiybrpdgo00ellssnc1qf2frg0elm12tlvxvu576i02hnjhmsqdctyl2326x26taa89v6iy1t8qi4il6ex0lra1kajvcgnezr3p6alplbng7ib9eaijubpdwn0b9ssjquaq2yqrc7yslxreyl0ua0zla9kqhm2r9a0d6aaxahmrb1qvapikl6zsxl7orrfjo6qfsf56lqzqhxfbt4clhaaysrhhpogzjusf63obiydx4h7c08tq2fh0ldz8vajhvid6ait03n2j6u1aa3ldmwsffv71ajdtneioloon61ym85f4otxed7d68vdv3l7d2eo85jw3v1ummnlic1pjydxjjgdmpol6ljdbvi7nkreyg9y01n2f435njdmxg7wesa7jkpv16t5dqfh6sw77epvh1vl4armgkczlwlk8j439pcls0mqdnnkmvwjneifnj7v3qsw3ap2omsozdzxd258cop5qreesugr9dtj0dqitr9ob67qiyfijo2ruuz2cu0p5dkngp70jqexo8n56ym592aaey1iolsrkszpbafy0zo9ft1fpnjodqbgwrsdkmsm4sd929hsqd84vddnpnqzfxyfmbzxjqa3s5xdebq8tnohkigsbnx20qq4lk2uf0quigzaawp5fa4ftqv0fxmg6cndg1cdazhx963pcin36d56xcr6l9hx6094y4afg4ntlq3ckb6y6g2x8rjhaa1u56v408ywomq5ohrs4w3t64x4il768o1otcg98cmsekc8nin5kqeiniu6fer452107sa1ono3pwzxj1lac3r8dh0l3yz290p5ac22bk8vvr2pi0ew50675166mfpehxj0i4f2nxo27ql4mmhnixdc4zi54ald5m9fg1o9bm51pnrdp4lmi8ubnmj3tgn85nqgkgqabou6hkgdvzaw7254kol6ay5k2fv1sl8qo5z43qs06fh17cipm6o7xuoquy19nrktopkpznwmcwibxmnc3gnnjpnrq6tofhl99o5zm9q3a74x2i8teuu9v5gdasitb3s5s4zin2q79oro0u7v20zld55q9ia5fwena43mg1zqphscnw3vucq5w47colh858iyw9dt8mzs8g55c67w15pgl4bpzbh005xd06uoqcpcrj9jhp1lrkcqtya7umkn5eryhvhald0tec13c2qn72kgwokkx9tzpltxwo36s5ufjc6eauizlj8p32xisecp4f5s9eg7k9uhzdz5dp9urzuda4yxlkucsecmp8v6cqbbty58dv5pihv1vfn3qxn6pvsloxzatmjx4swzxedujngtdoreho4r15y0s4q3z8cf87thf9inxyo00wtmi5oofp40w2ywuew22cpgw9utb59k2gotpq1fpelc4e308enmt2fbutgnsja6xidf28qvuq1cu4gdhur4npq2vvrdeq83zz1kxhgt3td7u78gtctwbrq7w4bpfqkbzinkgupsyprwzsrev3cr6afvrtdrmctucmgrcxdewfm7dz2qf0flyra304p49ek232yo1wu3vxpi6n1fsy2ywyvaspu5apkz0pdug2b1hoq66mk1kh5rg9l8j7o5yrpgeijw20kuwcbf9wmi2hhbh6vbpxichlj1x9wcrs2hl2wxluf72zyxmqrqacslae3cbnaipbf94qjyao8yi1789c2xqzrojalmgvwfgr0kmm5cknfedp76784clkeurckexntxkju169ag6dkdkzau8sej0v9g79q3ppb2nrn9djbd44de2jbed3broz66vls2pvlkzk9mwwf7zlh3m1yz9m5s0onvveq0rrrmh2pamhcvyb9lhxvv06bar85klwtehy0qlt3zefp3pofwspz6wlkrje06v6s1sn1t1nvtfp1lw089svf3a8tv5bw38m6ooqzyrhclithpvwglx1osma8k3fkt6eaqspsmu089rkdb89jn6q76ppgsis0dh58au81tripoai5s5jpxlkfgaxydl8rl9s14wiiwr0iksxa72lqv4b0rnzwhmh72ei15rzskaymbse9miez6uh8k2cgu5la1lfo3a21zlwff0dyhb52zwpjq14836a6jct3b7b76w55s3e24pavbcwmym8z6grys3nubwo2098zxdc5wiuwyw05d98d37r9eo93ad3dl74vhfwwjm2v6b3lzsvdd49bop0azwy4aicjqannnza4q0nluhkuyu0rrfu3ildao0l7dje6gftos7adklmqop69ng9eovwrzb8hjz2luq1zvomgolsgw90mmkk5pumy52sl9b8kq2b722lzzu7a5jww2iggvgeyq2d1gstzcst6rzz8uda7rone1ozxbryfp7aax3m0ob4ed2ewmzt3aca6wpv47ktj95a5icmwmzuev4pk8pw6p5on6b9qvs6bt1ntpcko29v9skj6rt96dpugutjep7h1fpzuv3y2u4a1sc61zsuu6zam1tj6f35wignse6k8b8v9jvw9ra3p4gfqmk2tj25hg73rpefc7y7svy5iz95t4hkkiw6wjui5zyff9l9q7ohoz9306wg6c0hgm65717w4dvzua44t50i1vxox6lypwf3a855w0tuizxb4rlazhdg5gc5d1mypgj2qkj681ux7aho1nakqs686m2lleh6g8npmyxnj7o1dvwie4h575waeshzj83xzscdsvvky4gk5p7kz8dz32o1hmj4qsp1leri2rfb5bluv222eisxi4h8uvntgqpoq2kzvgu7fvnww4q4yts5gfcpxq14s0h2jo9zpu20nd73o9pv0ptqhzfg96be666zcbpkhc8vgp9p2lgloxsgt9g90fpkl7atoipj55vhjkriw95kf9uwydqv7vbnk35rezx1durvx0ar8iagmhd1ghfzahjjzshl13l8mljujk2u1titvngams63jpvn8g7t8szspbqev18jcbhg4rcxck2kypg2wqpohjpv8xkebc2i4dac3ws0qw4rhex0g15rdnzziv7296yanor5wcaxcfske4ks1fcmz9rokmrksy1p5hjzjq78pkuxgb85tvdgxyijmbfi6y1t2zonfg1l9xm1xgwc7fk66nr0akec70wvlne1omy7ystl2gahyufvqltb2mtixoc348p113i77q2q5gx0cmn9dw2xqlf35b3mi2ejh8i88xdxeqwyoxfuiyteizployk7cg2ytb7f398xflj9nzwt4fvrt6c1oo0egthwhtmkuscapti2vivodds1od78z68h7c6r30y7j7ve9qh7voz56abng73uri9eyawi25ko3sk7nzlovmjf1olg0r38urzjcz48b3gz0e3fatybdie5q9vvujrsjlio8j5pkrc7l4jn0lry23exxcpfxp9lkr7aidbcgjb4ku992vd4351kc3ogk29fwm7q6zct1qd3sk8w4grdzvo0foysdfp3zzbp8uaouh38p26e4dx8qufp7wwo1uczyjdkihvad2lqfm1z2504k7rrjxt615hnin97kwly8ay5pm435cvw5c6gwsbclg3ks88r1usjyqh4nvmorv92flqjyt3qjul0cg3yslkbdiebetajps0m7kzowxi7z6h82h4h0zn7w4lladk30xn8m86g42feb18wmv1gogd25fsb9rid51oxc386tuka2v552027lsd5l1m4xdjpyzdx25vqins8nlwkgdj6gmkpc3hxvdpow29m7v9fdzdeu5iieosk1fjq3d0tda9qhpojk26a12iqdyko0nsip0076puqudl5ehkzj61rvxlg3vv8qly2gco8uozqguzj63qzp3j5ajt15ohag45tixjavyn6i8w7hgzp5jbpzr2b3pcchkehf8xn1ceakpao6pk4zueal9pvcttqnr3rudeqjoidua11qumdmxbxeajb2w46zi8m1bmkg");
        int c = 0;
        for (int i = 0; i < a.stateList.size(); i++) {
            if (a.stateList.get(i).code() > '0') {
                System.out.println(i);
                c++;
            }
        }
        List<DefaultKeyValue<String, List<State>>> stateListGroup = a.getStateListGroup("2023-06", BillingGranularityConstants.DAY);

        List<State> stateList1 = a.getStateList(null, Time.of(30, 10, 35));
        List<State> stateList2 = a.getStateList(Time.of(30, 10, 35), null);
        System.out.println(c);
    }

    /**
     * 实例状态空构造器
     */
    public InstanceState() {
        stateList = new ArrayList<>();
        this.mark = 0;
    }

    /**
     * 实例状态构造器
     *
     * @param state 状态数据
     */
    public InstanceState(String state) {
        String history = new BigInteger(state, 36).toString(this.radix);
        history = history.substring(1);
        this.mark = history.length();
        String reverseData = new StringBuilder(history).reverse().toString();
        this.stateList = toList(reverseData.toCharArray());
    }

    private List<State> toList(char[] chars) {
        List<State> instanceStateConstants = new ArrayList<>();
        for (char aChar : chars) {
            instanceStateConstants.add(new State(aChar));
        }
        return instanceStateConstants;
    }


    public String toDetailsString() {
        return this.stateList.stream().map(State::code).map(Object::toString).collect(Collectors.joining()) + '1';
    }

    @Override
    public String toString() {
        return new BigInteger(new StringBuilder(toDetailsString()).reverse().toString(), this.radix).toString(36);
    }

    /**
     * 修改
     *
     * @param start 开始下标
     * @param end   结束下标
     * @param state 状态
     */
    private void update(int start, int end, State state) {
        if (stateList.size() < start) {
            for (int i = stateList.size(); i < start; i++) {
                stateList.add(new State('0'));
            }
        }
        for (int i = start; i < (start == end ? end + 1 : end); i++) {
            if (i < this.stateList.size()) {
                stateList.set(i, state);
            } else {
                stateList.add(state);
            }

        }
        this.mark = stateList.size();
    }


    /**
     * 获取状态数据
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 状态
     */
    public List<State> getStateList(Time start, Time end) {
        int startIndex = Objects.isNull(start) ? 0 : Region.ofMinute(start.getDay(), start.getHour(), start.getMinute()).start;
        int endIndex = Objects.isNull(end) ? this.stateList.size() : Region.ofMinute(end.getDay(), end.getHour(), end.getMinute()).end;
        if (this.stateList.size() < endIndex) {
            endIndex = this.stateList.size() - 1;
        }
        if (this.stateList.size() < startIndex) {
            return List.of();
        }
        return this.stateList.subList(startIndex, endIndex);
    }

    public List<DefaultKeyValue<String, List<State>>> getStateListGroup(String month,
                                                                        BillingGranularityConstants granularity) {
        return getStateListGroup(null, null, month, granularity);
    }

    /**
     * @param start       开始时间
     * @param end         结束时间
     * @param month       月份
     * @param granularity 根据月份分组
     * @return 粒度 -> 状态
     */
    public List<DefaultKeyValue<String, List<State>>> getStateListGroup(Time start,
                                                                        Time end,
                                                                        String month,
                                                                        BillingGranularityConstants granularity) {
        if (granularity.equals(BillingGranularityConstants.MONTH)) {
            return List.of(new DefaultKeyValue<>(month, this.getStateList(start, end)));
        } else if (granularity.equals(BillingGranularityConstants.DAY)) {
            return groupDay(getStateList(start, end), start, month);
        } else if (granularity.equals(BillingGranularityConstants.HOUR)) {
            return groupHour(getStateList(start, end), start, month);
        } else {
            throw new RuntimeException("不支持的粒度");
        }
    }


    /**
     * 根据小时分组
     *
     * @param stateList 需要分组的数据
     * @param start     开始时间
     * @return 根据小时分组后的数据
     */
    private List<DefaultKeyValue<String, List<State>>> groupHour(List<State> stateList, Time start, String month) {
        if (Objects.isNull(start)) {
            start = Time.of(0, 0, 0);
        }
        String format = "yyyy-MM-dd HH";
        LocalDateTime startDateTime = CommonUtil.getLocalDateTime(month + "-" + start.day + " " + start.hour, format);
        List<DefaultKeyValue<String, List<State>>> result = new ArrayList<>();
        int startIndex = 60 - start.minute;
        if (startIndex != 0) {
            result.add(new DefaultKeyValue<>(startDateTime.format(DateTimeFormatter.ofPattern(format)), stateList.subList(0, Math.min(stateList.size(), startIndex))));
        }
        List<State> tmp = new ArrayList<>();
        for (int i = startIndex; i < stateList.size(); i++) {
            tmp.add(stateList.get(i));
            if (tmp.size() == 60) {
                startDateTime = startDateTime.plusHours(1);
                result.add(new DefaultKeyValue<>(startDateTime.format(DateTimeFormatter.ofPattern(format)), tmp));
                tmp = new ArrayList<>();
            } else if (i == stateList.size() - 1) {
                startDateTime = startDateTime.plusHours(1);
                result.add(new DefaultKeyValue<>(startDateTime.format(DateTimeFormatter.ofPattern(format)), tmp));
            }
        }
        return result;
    }

    /**
     * 根据小时分组
     *
     * @param stateList 需要分组的数据
     * @param start     开始时间
     * @return 根据天分组后的数据
     */
    private List<DefaultKeyValue<String, List<State>>> groupDay(List<State> stateList, Time start, String month) {
        if (Objects.isNull(start)) {
            start = Time.of(1, 0, 0);
        }
        String format = "yyyy-MM-dd";
        LocalDateTime startDateTime = CommonUtil.getLocalDateTime(month + "-" + start.day, format);
        List<DefaultKeyValue<String, List<State>>> result = new ArrayList<>();
        int startIndex = 60 * 24 - start.minute;
        if (startIndex != 0) {
            result.add(new DefaultKeyValue<>(startDateTime.format(DateTimeFormatter.ofPattern(format)), stateList.subList(0, Math.min(stateList.size(), startIndex))));
        }
        List<State> tmp = new ArrayList<>();
        for (int i = startIndex; i < stateList.size(); i++) {
            tmp.add(stateList.get(i));
            if (tmp.size() == 60 * 24) {
                startDateTime = startDateTime.plusDays(1);
                result.add(new DefaultKeyValue<>(startDateTime.format(DateTimeFormatter.ofPattern(format)), tmp));
                tmp = new ArrayList<>();
            } else if (i == stateList.size() - 1) {
                startDateTime = startDateTime.plusDays(1);
                result.add(new DefaultKeyValue<>(startDateTime.format(DateTimeFormatter.ofPattern(format)), tmp));
            }
        }
        return result;
    }

    /**
     * 修改一天中的计费状态
     * 注意包含开始时间 不包含结束时间 如果开始等于结束则不生效
     *
     * @param start 开始时间
     * @param end   结束时间
     * @param state 状态
     */
    public void update(Time start, Time end, State state) {
        int startIndex = Objects.isNull(start) ? 0 : Region.ofMinute(start.getDay(), start.getHour(), start.getMinute()).start;
        int endIndex = Objects.isNull(end) ? this.stateList.size() : Region.ofMinute(end.getDay(), end.getHour(), end.getMinute()).end;
        update(startIndex, endIndex, state);
    }

    /**
     * 修改一天中的计费状态
     * 注意包含开始时间 不包含结束时间 如果开始等于结束则不生效
     *
     * @param end   结束时间
     * @param state 状态
     */
    public void updateHistory(Time end, State state) {
        int endIndex = Objects.isNull(end) ? this.stateList.size() : Region.ofMinute(end.getDay(), end.getHour(), end.getMinute()).end;
        update(this.mark, endIndex, state);
    }

    @Data
    public static class Time {
        /**
         * 日
         */
        private int day;
        /**
         * 小时
         */
        private int hour;
        /**
         * 分钟
         */
        private int minute;

        public static Time of(int day, int hour, int minute) {
            Time time = new Time();
            time.day = day;
            time.hour = hour;
            time.minute = minute;
            return time;
        }

        public static Time of(int hour, int minute) {
            Time time = new Time();
            time.day = 0;
            time.hour = hour;
            time.minute = minute;
            return time;
        }

        @Override
        public String toString() {
            String format = "%02d %02d:%02d:00";
            return format.formatted(day, hour, minute);
        }
    }


    static class Region {
        /**
         * 开始索引
         */
        private int start;
        /**
         * 结束索引
         */
        private int end;

        static Region ofDay(int day) {
            day = day - 1;
            Region region = new Region();
            region.start = day * 60 * 24;
            region.end = day * 60 * 24 + 60 * 24;
            return region;
        }

        static Region ofHour(int day, int hour) {
            day = day - 1;
            Region region = new Region();
            region.start = day * 60 * 24 + hour * 60;
            region.end = day * 60 * 24 + hour * 60 + 60;
            return region;
        }

        static Region ofMinute(int day, int hour, int minute) {
            check(day, hour, minute);
            day = day - 1;
            Region region = new Region();
            region.start = day * 60 * 24 + hour * 60 + minute;
            region.end = day * 60 * 24 + hour * 60 + minute;
            return region;
        }

        static void check(int day, int hour, int minute) {
            if (day < 1 || day > 31) {
                throw new RuntimeException("时间不对");
            }
            if (hour < 0 || hour > 23) {
                throw new RuntimeException("时间不对");
            }
            if (minute < 0 || minute > 59) {
                throw new RuntimeException("时间不对");
            }
        }
    }


    public record State(char code) {

        public State {
            if (code < 48 || code > 57) {
                throw new RuntimeException("实例状态只支持10种" + "'0'-'9'{48-57}");
            }
        }
    }
}
