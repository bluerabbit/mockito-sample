package study;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class FooTest {

	@Mock
	Foo mockFoo;

	@Test
	public void sayはnoneを返す() {
		assertEquals(new Foo().say(), "none");
	}

	@Test
	public void アノテーションでmockを使いsayを書き換える() {
		MockitoAnnotations.initMocks(this);
		when(mockFoo.say()).thenReturn("hello");
		assertEquals(mockFoo.say(), "hello");
	}

	@Test
	public void mockを使うとsayを書き換えられる() {
		Foo foo = mock(Foo.class);
		when(foo.say()).thenReturn("hello");
		assertEquals(foo.say(), "hello");
	}

	@Test
	public void stubでsayを書き換える() {
		Foo foo = mock(Foo.class);
		stub(foo.say()).toReturn("hello");
		assertEquals(foo.say(), "hello");
	}

	@Test(expected = RuntimeException.class)
	public void 強制的に例外を発生させる() {
		Foo foo = mock(Foo.class);
		doThrow(new RuntimeException()).when(foo).say();
		foo.say();
	}

	@Test(expected = RuntimeException.class)
	public void 強制的に例外を発生させるthenThrow() {
		Foo foo = mock(Foo.class);
		when(foo.say()).thenThrow(new RuntimeException());
		foo.say();
	}

	@Test(expected = RuntimeException.class)
	public void throwErrorはException発生() {
		new Foo().throwError();
	}

	@Test
	public void doNothingで実行されなかった事にする() {
		Foo foo = mock(Foo.class);
		doNothing().when(foo).throwError();
		foo.throwError();
	}

	@Test
	public void Answerを使って任意の処理を差し込めて戻り値も変更できる() {
		Answer<Object> ans = new Answer<Object>() {
			public String answer(InvocationOnMock inv) {
				Foo foo = (Foo) inv.getMock();
				foo.prop = "";
				for (Object arg : inv.getArguments()) {
					foo.prop += arg.toString();
				}
				return "mockito!";
			};
		};
		Foo foo = mock(Foo.class, ans);
		String answer = foo.fuga("arg1", "arg2");
		assertThat(answer, is("mockito!"));
		assertThat(foo.prop, is("arg1arg2"));
	}

	@Test
	public void doAnswerを使って任意の処理を差し込めて戻り値も変更できる() {
		Answer<Object> ans = new Answer<Object>() {
			public String answer(InvocationOnMock inv) {
				Foo foo = (Foo) inv.getMock();
				foo.prop = "";
				for (Object arg : inv.getArguments()) {
					foo.prop += arg.toString();
				}
				return "mockito!";
			};
		};
		Foo foo = mock(Foo.class);
		doAnswer(ans).when(foo).fuga("arg1", "arg2");
		String answer = foo.fuga("arg1", "arg2");
		assertThat(answer, is("mockito!"));
		assertThat(foo.prop, is("arg1arg2"));
	}

	@Test
	public void 引数が意図した値になっているかをverifyで検証する() {
		Foo foo = spy(new Foo());
		foo.fuga("a", "b");
		verify(foo).fuga("a", "b");
	}
}
