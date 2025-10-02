# アプリケーションアーキテクチャ設計パターン

## スコープ

- リクエストスコープ
  - 1つのリクエスト（HTTPリクエスト受信からHTTPレスポンス送信まで）に結び付く記憶領域。
  - JavaEEでは`HttpServletRequest`によって提供される。

  ``` Java
    // データ格納
    request.setAttribute("foo", foo);

    // データの取得
    Foo foo = (Foo) request.getAttribute("foo");
  ```

- セッションスコープ
  - 1つのHTTPセッション（通常はログインからログアウトまで）に結び付く記憶領域。
  - 同一ユーザの情報を複数リクエストに跨って保持するためのもの。
  - JavaEEでは`HttpSession`によって提供される。

  ``` Java
    // 明示的にインスタンスを取得する必要がある。
    HttpSession session = request.getSession();

    // データの格納
    session.setAttribute("foo", foo);

    // データの取得
    Foo foo = (Foo) session.getAttribute("foo");
  ```

- アプリケーションスコープ
  - 1つのWebアプリケーション（開始から停止まで）に結び付く記憶領域。
  - 複数ユーザ間に跨ったデータを保持するためのもの。
  - JavaEEでは`ServletContext`によって提供される。

  ``` Java
    // 明示的にインスタンスを取得する必要がある。
    ServeletContext sc = getServletContext();

    // データの格納
    sc.setAttribute("foo", foo);

    // データの取得
    Foo foo = (Foo) sc.getAttribute("foo");
  ```

### サーブレット・JSPページの連携方法

- ディスパッチ方式

``` mermaid
flowchart LR
  browser["Webブラウザ"]

  subgraph JavaEEコンテナ
    forward_src["フォワード元<br>サーブレット・JSP"]
    forward_dst["フォワード先<br>サーブレット・JSP"]
  end

  browser -- HTTPリクエスト --> forward_src;
  forward_src -- 処理を委譲 --> forward_dst;
  forward_dst -- HTTPレスポンス --> browser;
```

``` Java
  RequestDispatcher rd = request.getRequestDispatcher("/FooPage");
  rd.forward(request, response);
```


- インクルード方式

``` mermaid
flowchart LR
  browser["Webブラウザ"]

  subgraph JavaEEコンテナ
    forward_src["フォワード元<br>サーブレット・JSP"]
    forward_dst["フォワード先<br>サーブレット・JSP"]
  end

  browser -- HTTPリクエスト --> forward_src;
  forward_src --> forward_dst;
  forward_dst -- 出力を合成 --> forward_src;
  forward_src -- HTTPレスポンス --> browser;
```

``` Java
  RequestDispatcher rd = request.getRequestDispatcher("/FooServlet");
  rd.include(request, response);
```

- リダイレクト方式

``` mermaid
flowchart LR
  browser["Webブラウザ"]

  subgraph JavaEEコンテナ1
    redirect_src["リダイレクト元<br>サーブレット・JSP"]
  end

  subgraph JavaEEコンテナ2
    redirect_dst["リダイレクト先<br>サーブレット・JSP"]
  end

  browser -- HTTPリクエスト --> redirect_src;
  redirect_src -- HTTPレスポンス<br>(301 or 302) --> browser;
  browser -- HTTPリクエスト --> redirect_dst;
  redirect_dst -- HTTPレスポンス --> browser;
```

``` Java
  response.sendRedirect(request.getContextPath() + "/RedirectPage");
```

## 4.2.2 フィルタ

- Webブラウザから送信されたHTTPリクエストをインターセプトし、サーブレット・JSPページの呼び出し前後に任意の処理を組み込むためのコンポーネント。

``` mermaid
flowchart LR
  browser["Webブラウザ"]

  subgraph JavaEEコンテナ
    filter["フィルタ"]
    servlet["呼び出し対象のサーブレット・JSP"]
  end

  browser -- HTTPリクエスト --> filter;
  filter -- HTTPリクエスト --> servlet;
  servlet -- HTTPレスポンス --> filter;
  filter -- HTTPレスポンス --> browser;
```

``` Java
  @WebServlet("/PersonServlet")
  public class PersonFilter extends Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
      // 何らかの前処理を行う

      // HTTPリクエスト・HTTPレスポンスを転送する
      chain.doFilter(req, res);

      // 何らかの後処理を行う
    }
  }
```

## 4.2.4 他システムとの連携方式

- リダイレクト方式

``` mermaid
flowchart LR
  browser_src["Webページ(遷移元)"]
  browser_dst["Webページ(遷移先)"]

  subgraph JavaEEコンテナ1
    servlet_src["コントローラ(Servlet)"]
  end

  subgraph JavaEEコンテナ2
    servlet_dst["ビュー(JSP)"]
  end

  browser_src -- HTTPリクエスト --> servlet_src;
  servlet_src -- HTTPレスポンス<br>(302 or 303) --> browser_src;
  browser_src -- HTTPリクエスト --> servlet_dst;
  servlet_dst -- HTTPレスポンス --> browser_dst;

  browser_src -- 画面遷移 --> browser_dst;
```

- 空ページ自動サブミット方式
  - windowオブジェクトのonloadプロパティにイベントハンドラを設定することによって、空ページがロードされた直後に自動的にフォームをサブミットする。
  - リダイレクト方式と異なり、POSTメソッドで他システムと連携できる点が特徴。

``` HTML
  <script type="text/javascript">
    window.onload = function() {
      document.forms[0].submit();
    }
  </script>

  <body>
    <form action="連携先のURL" method="POST">
      <!-- inputタグはhiddenにして非表示にする -->
      <input type="hidden" name="personName" value="${personName}">
      <input type="hidden" name="age" value="${personAge}">
    </form>
  </body>
```

## 4.3 セッション管理方式

- TODO: 大規模システム向けの内容なので優先度を下げて、次回まとめる。

## 4.6 認証とログイン・ログアウト

### 4.6.1 認証と認可

- 実現方式
  - 1. HTTPの仕様で規定された認証機能を利用する方式
  - 2. JavaEEコンテナ固有の認証機能を利用する方式
  - 3. アプリケーションとして実装する方式
  - 4. SSOサーバで認証する方式

- 1. HTTPの仕様で規定された認証機能を利用する方式
  - BASIC認証またはDIGEST認証を利用する。ここではBASIC認証の仕組みを説明する。
    - 1. 保護されたコンテンツにユーザがアクセスしようとすると、ステータスコード401(`Unauthorized`)が返ってくる。
    - 2. Webブラウザは、401を受け取ると専用のダイアログを出力し、ユーザIDとパスワードの入力を促す。
    - 3. Webブラウザは、入力されたユーザIDとパスワードをBASE64でエンコードし、`Authorization`ヘッダに付与して送信する。
    - 4. サーバサイドは、ヘッダからユーザIDとパスワードを取り出し、リポジトリを参照してパスワードの突合せを行う。
    - 5. 一度認証がOKになると、以降、Webブラウザは毎回同じ`Authorization`ヘッダを送信する。
  
  - BASIC認証方式の特徴
    - チケット（証明書）やセッション変数によってログイン状態を管理するわけではないため、ログアウトという概念は無い。
    - 簡易的な認証方式としてテスト環境で使うことはあるが、ログイン状態の管理ができない、パスワードが平文で送られるなどの課題があるため、本番環境で使うことは少ない。

- 2. JavaEEコンテナ固有の認証機能を利用する方式
  - ログインの流れ
    - 1. 開発者は、保護したいリソースをJavaEEコンテナの設定ファイルに定義する。
    - 2. 開発者は、ユーザIDとパスワード入力画面をアプリケーションとして作成する。
    - 3. JavaEEコンテナは、ユーザIDとパスワードを含むリクエストを受信すると認証を行う。
    - 4. 認証がOKとなると、JavaEEコンテナは当該ユーザが「認可済み」であることを管理する。
    - 5. 以降のリクエストに対しては、JavaEEコンテナが自動的に認可のチェックを行う。
  
  - ログアウトの流れ
    - 明示的なログアウト
      - 1. ユーザがログアウトボタンを押下する。
      - 2. アプリケーションがセッション変数を無効化する。or `HttpServletRequest`の`logout`メソッドによって「認証済み」であることを無効化する。
    - 暗黙的なログアウト
      - 1. ログイン後、無操作の状態が一定時間続く。
      - 2. JavaEEコンテナは、セッション変数の無操作タイムアウトによって自動的にセッション変数を無効化する。（＝ログアウト）

- 3. アプリケーションとして認証を実装する方式
  - ログイン
    - 1. 開発者は、ユーザIDとパスワード入力画面をアプリケーションとして作成する。
    - 2. 開発者は、入力されたパスワードとリポジトリに登録されたパスワードの突合せ処理もアプリケーションとして実装する。
    - 3. 認証がOKとなった場合、アプリケーションは「ユーザにアクセス権を与えたことを表すフラグ（認可済みフラグと呼称）」をセッション変数に格納する。
    - 4. 以降のリクエストでは、毎回セッション変数を確認することで認可のチェックを行う。
      - この処理は全てのリクエストに対して共通的に行う必要があるため、通常はフィルタで実行する。
      - セッション変数がログアウトやタイムアウトによって破棄されていた場合は、ログイン画面にディスパッチする。
  - ログアウト
    - 明示的なログアウト
      - ログアウトボタン押下を契機に、アプリケーションがセッション変数から認可済みフラグを削除する。
    - 暗黙的なログアウト
      - タイムアウトのケースでは他の方式と同様に、セッション変数の無操作タイムアウトによって自動的にセッション変数を削除する。

- 4. SSOサーバで認証する方式
  - リバースプロキシ型
  - エージェント型

### 4.6.7 二重ログインチェック

- 前提として、「1人のユーザが、2つのクライアント端末から同時にログインすることを許容しない」という業務要件があるとする。
- 実現方法
  - RDBの所定のテーブルにユーザIDとセッションIDのペアを格納し、リクエストを受ける度に、ユーザIDをキーにセッションIDが一致するかどうかチェックする。
  - あるクライアント端末からログインがあったときに、当該ユーザが既に別の端末でログインしていた場合（ログインテーブルに当該ユーザのデータが存在し、且つセッションIDが異なる）、通常は後勝ちとして、セッションIDを上書きする。
  - 先にログインしていた端末では、後続のログインチェックにおいてセッションIDが不一致になるため、セッション変数を無効化してログアウトする。

### 4.7.3 ウィンドウ制御

- ユーザがタブによって複数ウィンドウを起動するケースはよくある。
- このとき、Webブラウザは通常、複数ウィンドウ（タブ含む）でクッキーを共有する。
- したがって、複数画面から構成される対話形式のユースケースでは、Webブラウザとの会話情報を保持するためにセッション変数を利用すると、複数ウィンドウによる平行操作において、意図しないエラーやセッション情報の不整合を引き起こす可能性がある。

- 対策方法
  - 1. シングルサブウィンドウ方式
  - 2. マルチウィンドウ ＋ 不整合チェック方式
  - 3. マルチウィンドウ ＋ 並行操作可能方式

- 1. シングルサブウィンドウ方式
  - 概要
    - ユーザの利便性を割り切り、ウィンドウを1つしか開けないよう制限する。
  
  - 実装方法
    - 具体的には、当該の画面をJavaScriptによって「同名のサブウィンドウ」として立ち上げるようにすると、複数画面を同時に開くことを抑止できる。
    - さらに、サブウィンドウのアドレスバーをJavaScriptによって非表示にすると、アドレスバーにURLを直接入力することができなくなるため、より安全性が高まる。

- 2. マルチウィンドウ ＋ 不整合チェック方式
  - 概要
    - 複数ウィンドウを開くことは許容しつつも、ユーザがどちらかのウィンドウで次の画面に進もうとしたタイミングでチェックを行った不整合を防ぐ。
  
  - 実現方法
    - 具体的には、画面にランダムなIDを埋め込み、それをセッション変数で管理する。
    - ユーザが何らかの値を入力して次の画面に進もうとしたときに、送信されたウィンドウIDとセッション変数に格納されたウィンドウIDの一致をチェックし、不一致の場合はエラーとする。
  - セッション変数には、後から立ち上げたウィンドウのIDが格納されているため、2つのウィンドウは後勝ちになる。

- 3. マルチウィンドウ ＋ 並行操作可能方式
  - 概要
    - 複数のウィンドウを開くことを許容するだけでなく、両方のウィンドウで並行して次の画面に進む操作が可能。
  
  - 実現方法
    - 実現のためには、セッション変数（セッションスコープ）よりも小さい粒度のスコープが必要。
    - MVCフレームワークには、このマルチウィンドウ方式をサポートしたものがあり、セッションスコープより粒度の小さいカンバセーションスコープを提供している。

### 4.8 不正な更新リクエストの対策

- ユーザの誤操作による不正な更新リクエスト
  - 1. 更新処理を行った後、戻るボタンで前の画面に戻り、再びサブミットする。
  - 2. 更新処理を行った後、結果画面で更新ボタンを押下する。
    - 更新ボタンを押下すると、直前のリクエストを最送信する。
- 悪意ある第三者の攻撃による不正な更新リクエスト
  - 3. XSS攻撃
    - 攻撃者が用意したJavaScriptプログラムを標的となるWebサイトに送信させ、当該Webサイトの画面において当該プログラムを実行することによって、クッキーの搾取などを行う。
  - 4. CSRF攻撃
    - ユーザの正規のチケットを使って、Webアプリケーションに対して攻撃を仕掛ける。

    ``` html
      <body onload="document.forms[0].submit()">

      <form action="http://foo.bar:8080//BBSServlet" method="POST">
        <input type="hidden" name="title" value="悪意のあるタイトル">
        <input type="hidden" name="content" value="悪意のあるタイトル">
        <input type="submit">
    ```

- 対策
  - 1. 更新処理を行った後、戻るボタンで前の画面に戻り、再びコミットする。
    - Webブラウザのキャッシュ無効化
    - トークンチェック
    - カンバセーションスコープ
  - 2. 更新処理を行った後、結果画面で更新ボタンを押下する。
    - トークンチェック
    - POST-REDIRECT-GETによる画面遷移
    - カンバセーションスコープ
  - 3. XSS攻撃
    - サニタイジング
  - 4. CSRF攻撃
    - トークンチェック

  - Webブラウザのキャッシュ無効化
    - 入力画面においてキャッシュを無効化すると、戻るボタンによる不正なリクエストを抑止できる。
    - JSPでは以下のように記述することでキャッシュを無効化できる。

    ``` JSP
      <% response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
      %>
    ```

  - トークンチェック
    - サーバサイドにおいて、リクエストが適正であるかどうかトークンと呼ばれる乱数を使用して検証する。
    - 具体的には、サーバサイドにおいてトークンを振り出し、Webページの隠しフィールド(`<input type="hidden">`)にセットすると同時に、セッション変数にも格納する。次のリクエスト処理において、Webブラウザから送信されたトークンとセッション変数に格納されたトークンが一致しているかチェックする。
  
  - サニタイジング
    - javascriptの実行に必要ないくつかの特別な文字を、HTMLコードとして出力するときにエスケープすることでXSS攻撃を回避する。
    - エスケープの例
      - `<` -> `&lt`
      - `>` -> `&gt`
      - `"` -> `&quot`
      - `&` -> `&amp`
    
    - JSPでのサニタイジング方法
    
      ``` JSP
        <!-- 方法1 コアタグ(JSTL)を利用する方法 -->
        <c:out value="${someParam}" escapeXml="true" />

        <!-- 方法2 ファンクションタグ(JSTL)のescapeXml関数を利用する方法 -->
        <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
        ${fn:escapeXml(someParam)}
      ```
  