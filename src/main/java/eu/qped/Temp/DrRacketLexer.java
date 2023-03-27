// Generated from io\bitbucket\plt\autotutor\DrRacket.g4 by ANTLR 4.10.1
package eu.qped.Temp;

	import static org.apache.commons.lang3.StringEscapeUtils.escapeXml;
  	import java.util.List;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class DrRacketLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.10.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, TRUE=8, FALSE=9, 
		SYMBOL=10, QUOTE=11, NUMBER=12, INT=13, STRING=14, CHARACTER=15, LAMBDA=16, 
		NAME=17, HASH_NAME=18, WS=19, COMMENT=20;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "TRUE", "FALSE", 
			"SYMBOL", "QUOTE", "NUMBER", "INT", "STRING", "CHARACTER", "LAMBDA", 
			"NAME", "HASH_NAME", "WS", "COMMENT"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'['", "']'", "'`'", "','", "'#'", null, null, null, 
			"'''", null, null, null, null, "'\\u03BB'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, "TRUE", "FALSE", "SYMBOL", 
			"QUOTE", "NUMBER", "INT", "STRING", "CHARACTER", "LAMBDA", "NAME", "HASH_NAME", 
			"WS", "COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public DrRacketLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "DrRacket.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\u0004\u0000\u0014\u00ad\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011"+
		"\u0002\u0012\u0007\u0012\u0002\u0013\u0007\u0013\u0001\u0000\u0001\u0000"+
		"\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007\u0001\u0007"+
		"\u0001\u0007\u0003\u0007E\b\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001\b\u0001"+
		"\b\u0001\b\u0003\bV\b\b\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001"+
		"\u000b\u0003\u000b^\b\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001"+
		"\u000b\u0005\u000bd\b\u000b\n\u000b\f\u000bg\t\u000b\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0003\u000bo\b"+
		"\u000b\u0001\f\u0001\f\u0005\fs\b\f\n\f\f\fv\t\f\u0001\f\u0003\fy\b\f"+
		"\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0005\r\u0081\b\r\n\r"+
		"\f\r\u0084\t\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0003\u000e\u0092\b\u000e\u0001\u000f\u0001\u000f\u0001\u0010\u0004"+
		"\u0010\u0097\b\u0010\u000b\u0010\f\u0010\u0098\u0001\u0011\u0001\u0011"+
		"\u0001\u0011\u0001\u0012\u0004\u0012\u009f\b\u0012\u000b\u0012\f\u0012"+
		"\u00a0\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013\u0005\u0013\u00a7"+
		"\b\u0013\n\u0013\f\u0013\u00aa\t\u0013\u0001\u0013\u0001\u0013\u0000\u0000"+
		"\u0014\u0001\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006"+
		"\r\u0007\u000f\b\u0011\t\u0013\n\u0015\u000b\u0017\f\u0019\r\u001b\u000e"+
		"\u001d\u000f\u001f\u0010!\u0011#\u0012%\u0013\'\u0014\u0001\u0000\u0007"+
		"\u0001\u000009\u0001\u000019\u0001\u0000\"\"\u0003\u000009AZaz\u0012\u0000"+
		"\t\r  \"#\'),,;;[[]]``{}\u0085\u0085\u00a0\u00a0\u1680\u1680\u2000\u200a"+
		"\u2028\u2029\u202f\u202f\u205f\u205f\u3000\u3000\n\u0000\t\r  \u0085\u0085"+
		"\u00a0\u00a0\u1680\u1680\u2000\u200a\u2028\u2029\u202f\u202f\u205f\u205f"+
		"\u3000\u3000\u0002\u0000\n\n\r\r\u00bf\u0000\u0001\u0001\u0000\u0000\u0000"+
		"\u0000\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000"+
		"\u0000\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000"+
		"\u000b\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u000f"+
		"\u0001\u0000\u0000\u0000\u0000\u0011\u0001\u0000\u0000\u0000\u0000\u0013"+
		"\u0001\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017"+
		"\u0001\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b"+
		"\u0001\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f"+
		"\u0001\u0000\u0000\u0000\u0000!\u0001\u0000\u0000\u0000\u0000#\u0001\u0000"+
		"\u0000\u0000\u0000%\u0001\u0000\u0000\u0000\u0000\'\u0001\u0000\u0000"+
		"\u0000\u0001)\u0001\u0000\u0000\u0000\u0003+\u0001\u0000\u0000\u0000\u0005"+
		"-\u0001\u0000\u0000\u0000\u0007/\u0001\u0000\u0000\u0000\t1\u0001\u0000"+
		"\u0000\u0000\u000b3\u0001\u0000\u0000\u0000\r5\u0001\u0000\u0000\u0000"+
		"\u000fD\u0001\u0000\u0000\u0000\u0011U\u0001\u0000\u0000\u0000\u0013W"+
		"\u0001\u0000\u0000\u0000\u0015Z\u0001\u0000\u0000\u0000\u0017]\u0001\u0000"+
		"\u0000\u0000\u0019x\u0001\u0000\u0000\u0000\u001bz\u0001\u0000\u0000\u0000"+
		"\u001d\u0091\u0001\u0000\u0000\u0000\u001f\u0093\u0001\u0000\u0000\u0000"+
		"!\u0096\u0001\u0000\u0000\u0000#\u009a\u0001\u0000\u0000\u0000%\u009e"+
		"\u0001\u0000\u0000\u0000\'\u00a4\u0001\u0000\u0000\u0000)*\u0005(\u0000"+
		"\u0000*\u0002\u0001\u0000\u0000\u0000+,\u0005)\u0000\u0000,\u0004\u0001"+
		"\u0000\u0000\u0000-.\u0005[\u0000\u0000.\u0006\u0001\u0000\u0000\u0000"+
		"/0\u0005]\u0000\u00000\b\u0001\u0000\u0000\u000012\u0005`\u0000\u0000"+
		"2\n\u0001\u0000\u0000\u000034\u0005,\u0000\u00004\f\u0001\u0000\u0000"+
		"\u000056\u0005#\u0000\u00006\u000e\u0001\u0000\u0000\u000078\u0005#\u0000"+
		"\u000089\u0005t\u0000\u00009:\u0005r\u0000\u0000:;\u0005u\u0000\u0000"+
		";E\u0005e\u0000\u0000<=\u0005#\u0000\u0000=E\u0005t\u0000\u0000>?\u0005"+
		"#\u0000\u0000?E\u0005T\u0000\u0000@A\u0005t\u0000\u0000AB\u0005r\u0000"+
		"\u0000BC\u0005u\u0000\u0000CE\u0005e\u0000\u0000D7\u0001\u0000\u0000\u0000"+
		"D<\u0001\u0000\u0000\u0000D>\u0001\u0000\u0000\u0000D@\u0001\u0000\u0000"+
		"\u0000E\u0010\u0001\u0000\u0000\u0000FG\u0005#\u0000\u0000GH\u0005f\u0000"+
		"\u0000HI\u0005a\u0000\u0000IJ\u0005l\u0000\u0000JK\u0005s\u0000\u0000"+
		"KV\u0005e\u0000\u0000LM\u0005#\u0000\u0000MV\u0005f\u0000\u0000NO\u0005"+
		"#\u0000\u0000OV\u0005F\u0000\u0000PQ\u0005f\u0000\u0000QR\u0005a\u0000"+
		"\u0000RS\u0005l\u0000\u0000ST\u0005s\u0000\u0000TV\u0005e\u0000\u0000"+
		"UF\u0001\u0000\u0000\u0000UL\u0001\u0000\u0000\u0000UN\u0001\u0000\u0000"+
		"\u0000UP\u0001\u0000\u0000\u0000V\u0012\u0001\u0000\u0000\u0000WX\u0005"+
		"\'\u0000\u0000XY\u0003!\u0010\u0000Y\u0014\u0001\u0000\u0000\u0000Z[\u0005"+
		"\'\u0000\u0000[\u0016\u0001\u0000\u0000\u0000\\^\u0005-\u0000\u0000]\\"+
		"\u0001\u0000\u0000\u0000]^\u0001\u0000\u0000\u0000^n\u0001\u0000\u0000"+
		"\u0000_o\u0003\u0019\f\u0000`a\u0003\u0019\f\u0000ae\u0005.\u0000\u0000"+
		"bd\u0007\u0000\u0000\u0000cb\u0001\u0000\u0000\u0000dg\u0001\u0000\u0000"+
		"\u0000ec\u0001\u0000\u0000\u0000ef\u0001\u0000\u0000\u0000fh\u0001\u0000"+
		"\u0000\u0000ge\u0001\u0000\u0000\u0000hi\u0007\u0001\u0000\u0000io\u0001"+
		"\u0000\u0000\u0000jk\u0003\u0019\f\u0000kl\u0005/\u0000\u0000lm\u0003"+
		"\u0019\f\u0000mo\u0001\u0000\u0000\u0000n_\u0001\u0000\u0000\u0000n`\u0001"+
		"\u0000\u0000\u0000nj\u0001\u0000\u0000\u0000o\u0018\u0001\u0000\u0000"+
		"\u0000pt\u0007\u0001\u0000\u0000qs\u0007\u0000\u0000\u0000rq\u0001\u0000"+
		"\u0000\u0000sv\u0001\u0000\u0000\u0000tr\u0001\u0000\u0000\u0000tu\u0001"+
		"\u0000\u0000\u0000uy\u0001\u0000\u0000\u0000vt\u0001\u0000\u0000\u0000"+
		"wy\u00050\u0000\u0000xp\u0001\u0000\u0000\u0000xw\u0001\u0000\u0000\u0000"+
		"y\u001a\u0001\u0000\u0000\u0000z\u0082\u0005\"\u0000\u0000{|\u0005\"\u0000"+
		"\u0000|\u0081\u0005\"\u0000\u0000}~\u0005\\\u0000\u0000~\u0081\u0005\""+
		"\u0000\u0000\u007f\u0081\b\u0002\u0000\u0000\u0080{\u0001\u0000\u0000"+
		"\u0000\u0080}\u0001\u0000\u0000\u0000\u0080\u007f\u0001\u0000\u0000\u0000"+
		"\u0081\u0084\u0001\u0000\u0000\u0000\u0082\u0080\u0001\u0000\u0000\u0000"+
		"\u0082\u0083\u0001\u0000\u0000\u0000\u0083\u0085\u0001\u0000\u0000\u0000"+
		"\u0084\u0082\u0001\u0000\u0000\u0000\u0085\u0086\u0005\"\u0000\u0000\u0086"+
		"\u001c\u0001\u0000\u0000\u0000\u0087\u0088\u0005#\u0000\u0000\u0088\u0089"+
		"\u0005\\\u0000\u0000\u0089\u0092\u0007\u0003\u0000\u0000\u008a\u008b\u0005"+
		"#\u0000\u0000\u008b\u008c\u0005\\\u0000\u0000\u008c\u008d\u0005s\u0000"+
		"\u0000\u008d\u008e\u0005p\u0000\u0000\u008e\u008f\u0005a\u0000\u0000\u008f"+
		"\u0090\u0005c\u0000\u0000\u0090\u0092\u0005e\u0000\u0000\u0091\u0087\u0001"+
		"\u0000\u0000\u0000\u0091\u008a\u0001\u0000\u0000\u0000\u0092\u001e\u0001"+
		"\u0000\u0000\u0000\u0093\u0094\u0005\u03bb\u0000\u0000\u0094 \u0001\u0000"+
		"\u0000\u0000\u0095\u0097\b\u0004\u0000\u0000\u0096\u0095\u0001\u0000\u0000"+
		"\u0000\u0097\u0098\u0001\u0000\u0000\u0000\u0098\u0096\u0001\u0000\u0000"+
		"\u0000\u0098\u0099\u0001\u0000\u0000\u0000\u0099\"\u0001\u0000\u0000\u0000"+
		"\u009a\u009b\u0005#\u0000\u0000\u009b\u009c\u0003!\u0010\u0000\u009c$"+
		"\u0001\u0000\u0000\u0000\u009d\u009f\u0007\u0005\u0000\u0000\u009e\u009d"+
		"\u0001\u0000\u0000\u0000\u009f\u00a0\u0001\u0000\u0000\u0000\u00a0\u009e"+
		"\u0001\u0000\u0000\u0000\u00a0\u00a1\u0001\u0000\u0000\u0000\u00a1\u00a2"+
		"\u0001\u0000\u0000\u0000\u00a2\u00a3\u0006\u0012\u0000\u0000\u00a3&\u0001"+
		"\u0000\u0000\u0000\u00a4\u00a8\u0005;\u0000\u0000\u00a5\u00a7\b\u0006"+
		"\u0000\u0000\u00a6\u00a5\u0001\u0000\u0000\u0000\u00a7\u00aa\u0001\u0000"+
		"\u0000\u0000\u00a8\u00a6\u0001\u0000\u0000\u0000\u00a8\u00a9\u0001\u0000"+
		"\u0000\u0000\u00a9\u00ab\u0001\u0000\u0000\u0000\u00aa\u00a8\u0001\u0000"+
		"\u0000\u0000\u00ab\u00ac\u0006\u0013\u0001\u0000\u00ac(\u0001\u0000\u0000"+
		"\u0000\u000e\u0000DU]entx\u0080\u0082\u0091\u0098\u00a0\u00a8\u0002\u0006"+
		"\u0000\u0000\u0000\u0001\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}