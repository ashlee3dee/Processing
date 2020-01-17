package g4p.tool.controls;

import g4p.tool.ToolMessages;
import g4p.tool.gui.propertygrid.Validator;

import java.io.IOException;
import java.io.ObjectInputStream;

import javax.swing.event.TableModelEvent;

@SuppressWarnings("serial")
public class DTimer extends DBase {

	public int	 		_0691_initDelay = 1000;
	public String 		initDelay_label = "Initial delay (ms)";
	public String 		initDelay_tooltip = ">=1 millseconds";
	public Boolean 		initDelay_edit = true;
	public Boolean 		initDelay_show = true;
	public Validator 	initDelay_validator = Validator.getValidator(int.class, 1, 99999);

	public int 			_0692_interval = 1000;
	public String 		interval_label = "Interval (ms)";
	public String 		interval_tooltip = ">=1 millseconds";
	public Boolean 		interval_edit = true;
	public Boolean 		interval_show = true;
	public Validator 	interval_validator = Validator.getValidator(int.class, 1, 99999);

	public Boolean 		_0693_timer_starts  = false;
	public String 		timer_starts_label = "Start when created";
	public String 		timer_starts_tooltip = "false - will need to be started in your program";
	public Boolean 		timer_starts_edit = true;
	public Boolean 		timer_starts_show = true;
	public String 		timer_starts_updater = "updateTimerStart";

	public int 			_0694_repeats = 0;
	public String 		repeats_label = "Number of repeats";
	public String 		repeats_tooltip = "0 = repeat forever";
	public Boolean 		repeats_edit = true;
	public Boolean 		repeats_show = false;
	public Validator 	repeats_validator = Validator.getValidator(int.class, 0, 99999);


	public DTimer(){
		super();
		selectable = false;
		resizeable = false;
		moveable = false;
		
		componentClass = "GTimer";
		set_name(NameGen.instance().getNext("timer"));
		set_event_name(NameGen.instance().getNext(get_name()+ "_Action"));

		x_show = y_show = width_show = height_show = false;
		
//		name_label = "Variable name";
//		name_tooltip = "Java naming rules apply";
		name_edit = true;
//		eventHandler_edit = eventHandler_show = true;		
	}

	/**
	 * Get the event header
	 * @return
	 */
	protected String get_event_header(int n){
		return ToolMessages.build(METHOD_START_0, _0030_eventHandler, componentClass, 
				_0010_name, $(id[n])).replace('[', '{');
	}

	/**
	 * Get the event code if none then return generic message.
	 * 
	 * 
	 * Overridden in :- DTimer
	 * 
	 * @return event handler code
	 */
	protected String get_event_code(){ 
		String ev_code = Code.instance().get(id[0]);
		if(ev_code == null)
			return ToolMessages.build(CODE_NO_EVENT_PARAM, _0010_name, componentClass);
		else
			return ev_code; 
	}

	/**
	 * Get the creator statement var = new Foo(...);
	 * @return
	 */
	protected String get_creator(DBase parent, String window){
		String s;
		s = ToolMessages.build(CTOR_GTIMER, _0010_name, "this", "this", _0030_eventHandler, $(_0692_interval));
		if(_0691_initDelay != _0692_interval){
			s += ToolMessages.build(INIT_DELAY_TIMER, _0010_name, $(_0691_initDelay));			
		}
		if(_0693_timer_starts){
			if(_0694_repeats <= 0)
				s += ToolMessages.build(START_TIMER_0 ,_0010_name);
			else
				s += ToolMessages.build(START_TIMER_1, _0010_name, $(_0694_repeats));
		}	
		return s;
	}

	public void updateTimerStart(){
		System.out.println("update timer start done  ");
		repeats_show = _0693_timer_starts;
		propertyModel.createProperties(this);
		propertyModel.fireTableChanged(new TableModelEvent(propertyModel));
	}

	protected void read(){
		super.read();
	}

	private void readObject(ObjectInputStream in)
	throws IOException, ClassNotFoundException
	{
		in.defaultReadObject();
		read();
	}
	
}
