/*
 * $Id: mxApplication.js,v 1.16 2009/05/07 08:16:33 gaudenz Exp $
 * Copyright (c) 2006, Gaudenz Alder
 *
 * Defines the startup sequence of the application.
 *
 */
{

	/**
	 * Constructs a new application
	 */
	function mxApplication(config)
	{
		var hideSplash = function()
		{
			// Fades-out the splash screen
			var splash = document.getElementById('splash');
			
			if (splash != null)
			{
				try
				{
					mxEvent.release(splash);
					mxUtils.fadeOut(splash, 100, true);
				}
				catch (e)
				{
					splash.parentNode.removeChild(splash);
				}
			}
		}
		
		try
		{
			if (!mxClient.isBrowserSupported())
			{
				mxUtils.error('Browser is not supported!', 200, false);
			}
			else
			{
				var node = mxUtils.load(config).getDocumentElement();
				editor = new mxEditor(node);
				
				// Updates the window title after opening new files
				var title = document.title;
				var funct = function(sender)
				{
					document.title = title + ' - ' + sender.getTitle();
				};
				
				editor.addListener(mxEvent.OPEN, funct);
				
				// Prints the current root in the window title if the
				// current root of the graph changes (drilling).
				editor.addListener(mxEvent.ROOT, funct);
				funct(editor);
				  
				editor.propertiesWidth = 500;
				editor.urlPost = CONTEXT_PATH +"/jteap/wfengine/workflow/WorkflowAction!uploadProcessDefAction.do";
				
				if(window.dialogArguments != null){
					var cell = editor.graph.getModel().getRoot() ;
					var edit ;
					if(window.dialogArguments.flowId == null){
						edit = new mxCellAttributeChange(cell,"flowType", window.dialogArguments.name);
						editor.graph.getModel().execute(edit);
						edit = new mxCellAttributeChange(cell,"flowTypeId", window.dialogArguments.id);
						editor.graph.getModel().execute(edit);	
						hideSplash();
					} else {
						//editor.open(CONTEXT_PATH +"/jteap/wfengine/workflow/FlowConfigAction!getFlowConfigXmlAction.do?id="+window.dialogArguments.flowId) ;
						edit = new mxCellAttributeChange(cell,"flowId", window.dialogArguments.flowId);
						editor.graph.getModel().execute(edit);
					}
				}
				// Displays version in statusbar
				//editor.setStatus('mxGraph '+mxClient.VERSION);
				// Shows the application
				
			}
		}
		catch (e)
		{
			hideSplash();

			// Shows an error message if the editor cannot start
			mxUtils.alert('Cannot start application: '+e.message);
			throw e; // for debugging
		}
								
		return editor;
	}

}
